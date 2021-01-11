package com.mtdengineering.weatherappdemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtdengineering.weatherappdemo.LocationService;
import com.mtdengineering.weatherappdemo.R;
import com.mtdengineering.weatherappdemo.WeatherApplication;
import com.mtdengineering.weatherappdemo.dagger.AppComponent;
import com.mtdengineering.weatherappdemo.dagger.ViewModelProviderFactory;
import com.mtdengineering.weatherappdemo.models.WeatherInfo;
import com.mtdengineering.weatherappdemo.utils.Constant;
import com.mtdengineering.weatherappdemo.viewmodels.MainViewModel;

import java.io.IOException;

import javax.inject.Inject;

import static com.mtdengineering.weatherappdemo.utils.AppUtil.convertKelvinToCelsius;
import static com.mtdengineering.weatherappdemo.utils.AppUtil.isNetworkProviderAvailable;
import static com.mtdengineering.weatherappdemo.utils.AppUtil.showUserAlert;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = getClass().getSimpleName();

    private static final int LOCATION_REQUEST_CODE = 101;
    public static final String RECEIVER_KEY = "receiver";
    private static int count = 1;

    LocationManager lm;

    String latitude;
    String longitude;

    TextView tvName, tvDescription, tvTemperature, tvPressure, tvHumidity, tvVisibility, tv_no_data;
    RelativeLayout rlView;
    LinearLayout llProgressBar;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        injectFields();
        setViews();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(needRuntimePermission())
        {
            requestLocationPermission();
        }

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
        subscribeObservers();

        Log.d(TAG, "onCreate");
    }

    private void injectFields()
    {
        AppComponent appComponent = ((WeatherApplication) getApplication()).appComponent;
        appComponent.injectActivity(this);
    }

    private void setViews()
    {
        rlView = findViewById(R.id.rl_view);
        llProgressBar = findViewById(R.id.llProgressBar);

        tvName = findViewById(R.id.tv_name);
        tvDescription = findViewById(R.id.tv_desc);
        tvTemperature = findViewById(R.id.tv_temp);
        tvPressure = findViewById(R.id.tv_pressure);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvVisibility = findViewById(R.id.tv_visibility);
        tv_no_data = findViewById(R.id.tv_no_data);
    }

    private void subscribeObservers()
    {
        viewModel.getWeatherInfo().observe(this, weatherInfo ->
        {
            llProgressBar.setVisibility(View.GONE);

            if(weatherInfo != null)
            {
                tv_no_data.setVisibility(View.GONE);
                rlView.setVisibility(View.VISIBLE);

                tvName.setText(weatherInfo.getName());
                tvDescription.setText(weatherInfo.getWeather().get(0).getDescription());

                String temperature = convertKelvinToCelsius(String.valueOf(weatherInfo.getMain().getTemperature()));
                tvTemperature.setText(temperature);

                tvPressure.setText(weatherInfo.getMain().getPressure() + " hPa");
                tvHumidity.setText(weatherInfo.getMain().getHumidity() + "%");

                String visibility = String.valueOf(Float.valueOf(weatherInfo.getVisibility()) / 1000);
                tvVisibility.setText(visibility + " Km");
            }
            else
            {
                tv_no_data.setVisibility(View.VISIBLE);
                rlView.setVisibility(View.INVISIBLE);
                showUserAlert(MainActivity.this, "Error!", "An error occurred while retrieving weather information. Please try again later");
            }
        });

        viewModel.getError().observe(this, error ->
        {
            llProgressBar.setVisibility(View.GONE);

            tv_no_data.setVisibility(View.VISIBLE);
            rlView.setVisibility(View.INVISIBLE);

            String errorMsg = error; // Not to be displayed to the User but used for debugging
            showUserAlert(MainActivity.this, "Error!", "An error occurred while retrieving weather information. Please try again later");
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        startLocService();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        stopService(new Intent(this, LocationService.class));
        Log.d(TAG, "onPause");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_update:
                viewModel.getWeatherInfo(latitude, longitude, Constant.API_KEY);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            case R.id.action_start:
                startLocService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestLocationPermission()
    {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.i(TAG, "Location permission granted");
        }
        else
        {
            Log.i(TAG, "Location permission denied");

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Location permission is required to get current location!")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                makeRequest();
            }
        }
    }

    private void makeRequest()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == LOCATION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i(TAG, "Permission has been granted by user");
            }
            else
            {
                Log.i(TAG, "Permission has been denied by user");
            }
        }
    }

    private boolean needRuntimePermission()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void startLocService()
    {
        if(isNetworkProviderAvailable(this, lm))
        {
            MyResultReceiver myResultReceiver = new MyResultReceiver(null);

            Intent intent = new Intent(this, LocationService.class);
            intent.putExtra(RECEIVER_KEY, myResultReceiver);
            startService(intent);
        }
    }

    private class MyResultReceiver extends ResultReceiver
    {
        public MyResultReceiver(Handler handler)
        {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData)
        {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == LocationService.RESULT_CODE && resultData != null)
            {
                latitude = String.valueOf(resultData.getDouble(LocationService.LAT_KEY));
                longitude = String.valueOf(resultData.getDouble(LocationService.LNG_KEY));

                if(count == 1) // first time -> display
                {
                    runOnUiThread(() ->
                    {
                        llProgressBar.setVisibility(View.VISIBLE);
                        viewModel.getWeatherInfo(latitude, longitude, Constant.API_KEY);
                    });
                }

                count++; // Now count != 1 -> do not update information.
                         // Update will be done manually with update button as required.
            }
            else
            {
                runOnUiThread(() -> showUserAlert(MainActivity.this, "Location error!", "Error finding your location. Please try again later."));
            }
        }
    }
}


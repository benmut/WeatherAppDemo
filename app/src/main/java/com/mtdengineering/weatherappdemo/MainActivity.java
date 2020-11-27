package com.mtdengineering.weatherappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = getClass().getSimpleName();

    private static final int LOCATION_REQUEST_CODE = 101;
    public static final String RECEIVER_KEY = "receiver";

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(needRuntimePermission())
        {
            requestLocationPermission();
        }

        Log.d(TAG, "onCreate");
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
        MyResultReceiver myResultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra(RECEIVER_KEY, myResultReceiver);
        startService(intent);
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
                latitude = resultData.getDouble(LocationService.LAT_KEY);
                longitude = resultData.getDouble(LocationService.LNG_KEY);
            }
        }
    }
}

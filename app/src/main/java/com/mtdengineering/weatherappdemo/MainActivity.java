package com.mtdengineering.weatherappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = getClass().getSimpleName();

    private static final int LOCATION_REQUEST_CODE = 101;

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
}

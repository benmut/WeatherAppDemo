package com.mtdengineering.weatherappdemo;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import android.os.ResultReceiver;
import android.util.Log;

public class LocationService extends Service
{
    private final String TAG = getClass().getSimpleName();

    public static final int RESULT_CODE = 10;
    public static final int RESULT_CODE_FAILED = 15;
    public static final String LAT_KEY = "lat_key";
    public static final String LNG_KEY = "lng_key";

    LocationManager lm;
    MyLocationListener gpsListener;

    Looper looper;
    ResultReceiver resultReceiver;

    public LocationService()
    {
    }

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate");

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "Service onStartCommand");

        resultReceiver = intent.getParcelableExtra(MainActivity.RECEIVER_KEY);
        startLocation();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy");

        stopLocation();
    }

    private void startLocation()
    {
        HandlerThread thread = new HandlerThread("locThread");
        thread.start();

        looper = thread.getLooper();

        gpsListener = new MyLocationListener();

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsListener,looper);
    }

    private void stopLocation()
    {
        if(gpsListener != null)
        {
            lm.removeUpdates(gpsListener);
            gpsListener = null;
        }

        if(looper != null)
        {
            looper.quit();
            looper = null;
        }
    }

    public class MyLocationListener implements LocationListener
    {
        private final String TAG = getClass().getSimpleName();

        @Override
        public void onLocationChanged(Location location)
        {
            String provider = location != null ? location.getProvider() : " - ";
            double lat = location != null ? location.getLatitude() : 0.0d;
            double lng = location != null ? location.getLongitude() : 0.0d;
            float accuracy = location != null ? location.getAccuracy() : 0.0f;
            long time = location != null ? location.getTime() : 0L;
            int resultCode = location != null ? RESULT_CODE: RESULT_CODE_FAILED;

            StringBuilder sb = new StringBuilder(40);
            sb.append(provider).append(" | ");
            sb.append(lat).append("/").append(lng).append(" | ");
            sb.append(accuracy).append(" | ");
            sb.append(time);

            Bundle bundle = new Bundle();
            bundle.putDouble(LAT_KEY, lat);
            bundle.putDouble(LNG_KEY, lng);

            resultReceiver.send(resultCode, bundle);

            Log.d(TAG, "Location: " + sb.toString());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.d(TAG, "StatusChanged: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.d(TAG, "ProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.d(TAG, "ProviderDisabled: " + provider);
        }
    }
}

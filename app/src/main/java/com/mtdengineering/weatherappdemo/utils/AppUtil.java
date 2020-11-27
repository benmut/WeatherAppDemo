package com.mtdengineering.weatherappdemo.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

public class AppUtil
{
    public static boolean isGpsProviderEnabled(Context context, LocationManager lm)
    {
        boolean isAvailable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isAvailable)
        {
            AlertUserDialog dialog = new AlertUserDialog("Please Enable Location Services.");
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }

        return isAvailable;
    }

    public static boolean isAirplaneModeOff(Context context)
    {
        boolean isOff =  Settings.System.getInt(context.getContentResolver(), AIRPLANE_MODE_ON, 0) == 0;

        if(!isOff)
        {
            AlertUserDialog dialog = new AlertUserDialog("Please Disable Airplane Mode.");
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }

        return isOff;
    }

    public static boolean isInternetAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);

        boolean isAvailable = capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

        if(!isAvailable)
        {
            AlertUserDialog dialog = new AlertUserDialog("Please check your internet connection and try again.");
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }

        return isAvailable;
    }

    public static boolean isNetworkProviderAvailable(Context context, LocationManager lm)
    {
        return isGpsProviderEnabled(context, lm) &&
                isAirplaneModeOff(context) &&
                isInternetAvailable(context);
    }
}

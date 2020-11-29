package com.mtdengineering.weatherappdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

public class AppUtil
{
    public static boolean isGpsProviderEnabled(Context context, LocationManager lm)
    {
        boolean isAvailable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isAvailable)
        {
            AlertUserDialog dialog = new AlertUserDialog("Please Enable Location Services.",
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }

        return isAvailable;
    }

    public static boolean isAirplaneModeOff(Context context)
    {
        boolean isOff =  Settings.System.getInt(context.getContentResolver(), AIRPLANE_MODE_ON, 0) == 0;

        if(!isOff)
        {
            AlertUserDialog dialog = new AlertUserDialog("Please Disable Airplane Mode.",
                    Settings.ACTION_AIRPLANE_MODE_SETTINGS);
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
            AlertUserDialog dialog = new AlertUserDialog("Please check your internet connection and try again.",
                    Settings.ACTION_WIFI_SETTINGS);
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

    public static String convertKelvinToCelsius(String kelvin)
    {
        return Math.round(Float.valueOf(kelvin) - 273.15f) + "\u00B0";
    }

    public static void showUserAlert(Context context, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

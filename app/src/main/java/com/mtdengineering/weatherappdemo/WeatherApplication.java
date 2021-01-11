package com.mtdengineering.weatherappdemo;

import android.app.Application;

import com.mtdengineering.weatherappdemo.dagger.AppComponent;
import com.mtdengineering.weatherappdemo.dagger.DaggerAppComponent;

public class WeatherApplication extends Application
{
    public AppComponent appComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        appComponent = DaggerAppComponent.create();
    }
}

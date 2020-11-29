package com.mtdengineering.weatherappdemo;

import android.app.Application;

import com.mtdengineering.weatherappdemo.repositories.implementation.WeatherRepository;

public class WeatherApplication extends Application
{
    public Container container = new Container();

    class Container
    {
        WeatherRepository weatherRepository = new WeatherRepository();
    }
}

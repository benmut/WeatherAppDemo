package com.mtdengineering.weatherappdemo.repositories.interfaces;

import com.mtdengineering.weatherappdemo.models.WeatherInfo;
import java.io.IOException;

public interface IWeatherRepository
{
    WeatherInfo getWeatherInfo(String latitude, String longitude) throws IOException;
}

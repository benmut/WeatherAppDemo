package com.mtdengineering.weatherappdemo.repositories.interfaces;

import com.mtdengineering.weatherappdemo.models.WeatherInfo;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherRepository
{
    @GET("weather")
    Flowable<WeatherInfo> getWeatherInfo(@Query("lat") String latitude, @Query("lon")String longitude, @Query("appid") String api_key);
}

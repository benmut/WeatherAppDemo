package com.mtdengineering.weatherappdemo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherInfo
{
    private String id;
    private String name;
    private String timezone;
    private String visibility;

    @SerializedName("coord")
    private Coordinate coordinate;

    private Sys sys;
    private Wind wind;
    private Main main;

    @SerializedName("weather")
    private List<Weather> weathers;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getVisibility() {
        return visibility;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Sys getSys() {
        return sys;
    }

    public Wind getWind() {
        return wind;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weathers;
    }
}

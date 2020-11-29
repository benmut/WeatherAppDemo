package com.mtdengineering.weatherappdemo.models;

public class WeatherInfo
{
    private String name;
    private String description;
    private String temperature;
    private String pressure;
    private String humidity;
    private String visibility;

    public WeatherInfo(String name, String description, String temperature, String pressure, String humidity, String visibility)
    {
        this.name = name;
        this.description = description;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getTemperature()
    {
        return temperature;
    }

    public String getPressure()
    {
        return pressure;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public String getVisibility()
    {
        return visibility;
    }

    public void setTemperature(String temperature)
    {
        this.temperature = temperature;
    }

    public void setPressure(String pressure)
    {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }

    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }
}

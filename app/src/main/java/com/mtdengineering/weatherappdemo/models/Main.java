package com.mtdengineering.weatherappdemo.models;

import com.google.gson.annotations.SerializedName;

public class Main
{
    @SerializedName("temp")
    private float temperature;

    @SerializedName("feels_like")
    private float feelsLike;

    @SerializedName("temp_max")
    private float temperatureMax;

    @SerializedName("temp_min")
    private float temperatureMin;

    @SerializedName("pressure")
    private int pressure;

    @SerializedName("humidity")
    private int humidity;


    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}

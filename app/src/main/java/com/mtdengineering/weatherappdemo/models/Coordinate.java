package com.mtdengineering.weatherappdemo.models;

import com.google.gson.annotations.SerializedName;

public class Coordinate
{
    @SerializedName("lat")
    private float latitude;

    @SerializedName("lon")
    private float longitude;

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}

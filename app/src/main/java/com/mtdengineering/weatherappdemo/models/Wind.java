package com.mtdengineering.weatherappdemo.models;

import com.google.gson.annotations.SerializedName;

public class Wind
{
    @SerializedName("speed")
    float speed;

    @SerializedName("deg")
    int degree;
}


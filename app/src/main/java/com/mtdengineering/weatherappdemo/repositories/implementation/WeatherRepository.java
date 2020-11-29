package com.mtdengineering.weatherappdemo.repositories.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtdengineering.weatherappdemo.models.WeatherInfo;
import com.mtdengineering.weatherappdemo.repositories.interfaces.IWeatherRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class WeatherRepository implements IWeatherRepository
{
    private static String API_BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static String API_KEY = "53f9d8e4213222cf517d86dc406d67fc";

    @Override
    public WeatherInfo getWeatherInfo(String latitude, String longitude) throws IOException
    {
        String queryString = String.format("?lat=%s&lon=%s&appid=%s", latitude, longitude, API_KEY);

        URL url = new URL(API_BASE_URL + queryString);

        try (InputStream inputStream = url.openConnection().getInputStream())
        {
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext())
            {
                return parseToWeatherInfo(scanner.next());
            }
            else
            {
                return null;
            }
        }
    }

    private WeatherInfo parseToWeatherInfo(String response) throws IOException
    {
        if(response == null)
            return null;

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);

        JsonNode arrayWeather = jsonNode.get("weather");
        JsonNode weatherNode = arrayWeather.get(0);
        JsonNode mainNode = jsonNode.get("main");

        String description = weatherNode.get("description").asText();
        String temperature = mainNode.get("temp").asText();
        String pressure = mainNode.get("pressure").asText();
        String humidity = mainNode.get("humidity").asText();
        String visibility = jsonNode.get("visibility").asText();
        String name = jsonNode.get("name").asText();

        return new WeatherInfo(name, description, temperature, pressure, humidity, visibility);
    }
}

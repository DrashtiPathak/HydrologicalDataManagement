package com.example.drashtimuni.hydrologicaldatamanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherData {
    private Date day;
    private double latitude;
    private double longitude;
    private String modelType;
    private String weatherDataType;
    private double weatherData;

    public Date getDay() {
        return day;
    }

    public String getDayString(String dateFormat) {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
         return simpleDateFormat.format(day);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setDay(String dayString, String format) {
        try {
            this.day = new SimpleDateFormat(format).parse(dayString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getWeatherDataType() {
        return weatherDataType;
    }

    public void setWeatherDataType(String weatherDataType) {
        this.weatherDataType = weatherDataType;
    }

    public double getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(double weatherData) {
        this.weatherData = weatherData;
    }
}
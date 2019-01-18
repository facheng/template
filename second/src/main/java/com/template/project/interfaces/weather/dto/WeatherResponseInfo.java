package com.template.project.interfaces.weather.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by facheng on 19-1-18.
 */
public class WeatherResponseInfo implements Serializable {

    private static final long serialVersionUID = 7143310090785591047L;

    @JSONField(name = "HeWeather6")
    private List<WeatherDetail> weatherDetails = new ArrayList<>();

    public static WeatherResponseInfo emptyInstance() {
        return new WeatherResponseInfo();
    }

    public List<WeatherDetail> getWeatherDetails() {
        return weatherDetails;
    }

    public void setWeatherDetails(List<WeatherDetail> weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    @Override
    public String toString() {
        return "{" + "weatherDetails=" + weatherDetails + '}';
    }
}

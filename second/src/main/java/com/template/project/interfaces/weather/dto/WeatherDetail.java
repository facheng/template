package com.template.project.interfaces.weather.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by facheng on 19-1-18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherDetail implements Serializable {

    private static final long serialVersionUID = -6957569354018185654L;

    private WeatherBasic basic;

    private WeatherNow now;

    private List<WeatherLifestyle> lifestyle;

    private WeatherUpdateInfo update;

    public WeatherBasic getBasic() {
        return basic;
    }

    public void setBasic(WeatherBasic basic) {
        this.basic = basic;
    }

    public WeatherNow getNow() {
        return now;
    }

    public void setNow(WeatherNow now) {
        this.now = now;
    }

    public List<WeatherLifestyle> getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(List<WeatherLifestyle> lifestyle) {
        this.lifestyle = lifestyle;
    }

    public WeatherUpdateInfo getUpdate() {
        return update;
    }

    public void setUpdate(WeatherUpdateInfo update) {
        this.update = update;
    }
}

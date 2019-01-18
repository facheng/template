package com.template.project.interfaces.weather.dto;

import java.io.Serializable;

/**
 * Created by facheng on 19-1-18.
 */
public class WeatherUpdateInfo implements Serializable {

    private static final long serialVersionUID = -7341939440161234715L;

    /**
     * 当地时间，24小时制，格式yyyy-MM-dd HH:mm
     */
    private String loc;

    /**
     * UTC时间，24小时制，格式yyyy-MM-dd HH:mm
     */
    private String utc;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}

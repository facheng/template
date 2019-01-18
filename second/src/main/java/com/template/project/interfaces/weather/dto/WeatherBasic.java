package com.template.project.interfaces.weather.dto;

import java.io.Serializable;

/**
 * Created by facheng on 19-1-18.
 */
public class WeatherBasic implements Serializable {

    private static final long serialVersionUID = -6957569354018185654L;

    /**
     * 地区／城市名称
     */
    private String location;

    /**
     * 地区／城市ID
     */
    private String cid;

    /**
     * 地区／城市纬度
     */
    private String lat;

    /**
     * 地区／城市经度
     */
    private String lon;

    /**
     * 地区／城市的上级城市
     */
    private String parent_city;

    /**
     * 地区／城市所属行政区域
     */
    private String admin_area;

    /**
     * 地区／城市所属国家名称
     */
    private String cnty;

    /**
     * 地区／城市所在时区
     */
    private String tz;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}

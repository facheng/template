package com.template.project.interfaces.weather.dto;

import java.io.Serializable;

/**
 * Created by facheng on 19-1-18.
 */
public class WeatherNow implements Serializable {

    private static final long serialVersionUID = -6957569354018185654L;

    /**
     * 体感温度，默认单位：摄氏度
     */
    private String fl;

    /**
     * 温度，默认单位：摄氏度
     */
    private String tmp;

    /**
     * 实况天气状况代码
     */
    private String cond_code;

    /**
     * 实况天气状况描述
     */
    private String cond_txt;

    /**
     * 风向360角度
     */
    private String wind_deg;

    /**
     * 风向
     */
    private String wind_dir;

    /**
     * 风力
     */
    private String wind_sc;

    /**
     * 风速，公里/小时
     */
    private String wind_spd;

    /**
     * 相对湿度
     */
    private String hum;

    /**
     * 降水量
     */
    private String pcpn;

    /**
     * 大气压强
     */
    private String pres;

    /**
     * 能见度，默认单位：公里
     */
    private String vis;

    /**
     * 云量
     */
    private String cloud;

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCond_code() {
        return cond_code;
    }

    public void setCond_code(String cond_code) {
        this.cond_code = cond_code;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(String wind_deg) {
        this.wind_deg = wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
}

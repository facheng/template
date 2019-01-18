package com.template.project.interfaces.weather.controller;

import static com.template.common.constant.Constants.WEATHER_LIFESTYLE_URL_KEY;
import static com.template.common.constant.Constants.WEATHER_NOW_URL_KEY;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.template.common.utils.JsonUtils;
import com.template.common.utils.http.HttpUtils;
import com.template.framework.web.service.ConfigService;
import com.template.project.interfaces.weather.dto.WeatherResponseInfo;

/**
 * Created by facheng on 19-1-18.
 */
@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    @Resource
    private ConfigService configService;

    @Value("${weather.request.key}")
    private String weatherRequestKey;

    @RequestMapping("/now")
    public WeatherResponseInfo getNowWeather(@RequestParam("location") String location) {

        Map<String, Object> params = new HashedMap();
        params.put("location", location);
        params.put("key", weatherRequestKey);

        return JsonUtils.parserTo(HttpUtils.sendGet(configService.getKey(WEATHER_NOW_URL_KEY), params),
                WeatherResponseInfo.class);
    }

    @RequestMapping("/lifestyle")
    public WeatherResponseInfo getLifestyle(@RequestParam("location") String location) {

        Map<String, Object> params = new HashedMap();
        params.put("location", location);
        params.put("key", weatherRequestKey);

        return JsonUtils.parserTo(HttpUtils.sendGet(configService.getKey(WEATHER_LIFESTYLE_URL_KEY), params),
                WeatherResponseInfo.class);
    }
}

package com.template.project.interfaces.weather;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.template.project.interfaces.weather.dto.WeatherResponseInfo;

/**
 * Created by facheng on 19-1-18.
 */
public class WeatherJsonPaserTest {

    private String mockWeatherInfo = "{\n" + "    \"HeWeather6\": [\n" + "        {\n" + "            \"basic\": {\n"
            + "                \"cid\": \"CN101010100\",\n" + "                \"location\": \"北京\",\n"
            + "                \"parent_city\": \"北京\",\n" + "                \"admin_area\": \"北京\",\n"
            + "                \"cnty\": \"中国\",\n" + "                \"lat\": \"39.90498734\",\n"
            + "                \"lon\": \"116.40528870\",\n" + "                \"tz\": \"8.0\"\n" + "            },\n"
            + "            \"now\": {\n" + "                \"cond_code\": \"101\",\n"
            + "                \"cond_txt\": \"多云\",\n" + "                \"fl\": \"16\",\n"
            + "                \"hum\": \"73\",\n" + "                \"pcpn\": \"0\",\n"
            + "                \"pres\": \"1017\",\n" + "                \"tmp\": \"14\",\n"
            + "                \"vis\": \"1\",\n" + "                \"wind_deg\": \"11\",\n"
            + "                \"wind_dir\": \"北风\",\n" + "                \"wind_sc\": \"微风\",\n"
            + "                \"wind_spd\": \"6\"\n" + "            },\n" + "            \"status\": \"ok\",\n"
            + "            \"update\": {\n" + "                \"loc\": \"2017-10-26 17:29\",\n"
            + "                \"utc\": \"2017-10-26 09:29\"\n" + "            }\n" + "        }\n" + "    ]\n" + "}";

    @Test
    public void weatherParserTest() {
        System.out.println(JSON.parseObject(mockWeatherInfo, WeatherResponseInfo.class));
    }
}

package com.template.common.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by facheng on 19-1-18.
 */
public class JsonUtils {

    public static <T> T parserTo(String sourceText, Class<T> targetType) {
        return JSON.parseObject(sourceText, targetType);
    }
}

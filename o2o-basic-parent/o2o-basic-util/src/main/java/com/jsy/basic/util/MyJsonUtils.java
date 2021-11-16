package com.jsy.basic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类
 */
public class MyJsonUtils {
    private static final ObjectMapper objectMapper=  new ObjectMapper();

    public static JsonNode str2JsonNode(String str){
        try {
            return   objectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

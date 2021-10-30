package com.jsy.basic.util.utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.poi.ss.formula.functions.T;

public class ObjectMapperUtil {
    /**
     * 用于解决对象转换异常问题
     * @param object 需要实例化对象
     * @param clazz 对象字节码
     */
    public static void insObject(Object object, Class clazz){
        ObjectMapper mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.convertValue(object, clazz);
    }

}

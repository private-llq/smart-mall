package com.jsy.config;

 import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
 import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;

 /**
  * @Description: 去掉LocalDateTime带T的问题
  * @author: Zhang Chonghu
  * @Date: 2021/1/27 9:51
  * @Copyright: Xi'an Dian Tong Software Co., Ltd. All Rights Reserved.
  * @Version 1.0
  */
 @Configuration
 public class LocalDateTimeSerializerConfig {
     @org.springframework.beans.factory.annotation.Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
     private String pattern;
     @Bean
     public LocalDateTimeSerializer localDateTimeDeserializer() {
         return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
     }
     @Bean
     public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
         return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
     }

 }
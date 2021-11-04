package com.jsy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.jsy.mapper")
@EnableFeignClients
public class BackStageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackStageApplication.class,args);
    }

}

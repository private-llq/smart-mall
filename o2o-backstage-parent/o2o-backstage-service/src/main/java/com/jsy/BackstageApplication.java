package com.jsy;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.jsy.mapper")
@EnableFeignClients
@Component
//@EnableDistributedTransaction
public class BackstageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackstageApplication.class,args);
    }
}

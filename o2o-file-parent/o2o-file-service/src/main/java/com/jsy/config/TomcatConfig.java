package com.jsy.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
 
@Configuration
public class TomcatConfig {
 
/*    @Value("${spring.servlet.multipart.maxFileSize}")
    private Long maxFileSize;
    @Value("${spring.servlet.multipart.maxRequestSize}")
    private Long maxRequestSize;
 */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个数据大小
        // factory.setMaxFileSize(MaxFileSize); // KB,MB
        factory.setMaxFileSize(DataSize.ofMegabytes(1024));//mb
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(1024));//mb
        // factory.setMaxRequestSize(MaxRequestSize);
        return factory.createMultipartConfig();
    }
}
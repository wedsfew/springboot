package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件名：WebMvcConfig.java
 * 功能：Web MVC配置类，配置静态资源映射
 * 作者：CodeBuddy
 * 创建时间：2025-08-23
 * 版本：v1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    /**
     * 配置静态资源映射
     * 将请求映射到静态资源目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
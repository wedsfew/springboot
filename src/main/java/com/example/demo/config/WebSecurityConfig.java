package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 文件名：WebSecurityConfig.java
 * 功能：Web安全配置类，配置HTTP安全规则
 * 作者：CodeBuddy
 * 创建时间：2025-08-11
 * 版本：v1.0.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    /**
     * 配置HTTP安全规则
     * 在开发测试阶段，允许所有请求，禁用CSRF保护
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // 禁用CSRF保护，方便测试API
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()  // 允许所有请求，方便测试
            );
        
        return http.build();
    }
}
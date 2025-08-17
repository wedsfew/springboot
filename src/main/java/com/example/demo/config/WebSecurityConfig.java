package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * 文件名：WebSecurityConfig.java
 * 功能：Web安全配置类，配置HTTP安全规则
 * 作者：CodeBuddy
 * 创建时间：2025-08-11
 * 版本：v1.0.0
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    /**
     * 配置HTTP安全规则
     * 配置JWT认证过滤器，保护需要认证的API端点
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // 禁用CSRF保护，方便测试API
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 使用无状态会话，不使用Session
            )
            .authorizeHttpRequests(authorize -> authorize
                // 公开接口，无需认证
                .requestMatchers("/api/auth/**").permitAll()  // 认证相关接口
                .requestMatchers("/api/verification/**").permitAll()  // 验证码相关接口
                .requestMatchers("/api/admin/register").permitAll()  // 管理员注册接口
                .requestMatchers("/api/admin/login").permitAll()  // 管理员登录接口
                .requestMatchers("/api/test/**").permitAll()  // 测试接口，无需认证
               // .requestMatchers("/api/users/**").permitAll() 
                // 需要认证的接口
                .requestMatchers("/api/users/**").authenticated()  // 用户相关接口需要认证
                .requestMatchers("/api/admin/**").authenticated()  // 管理员相关接口需要认证
                .anyRequest().authenticated()  // 其他所有请求都需要认证
            )
            // 设置自定义认证入口点
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

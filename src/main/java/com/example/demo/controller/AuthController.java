package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.dto.UserRegisterVerifyRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 文件名：AuthController.java
 * 功能：认证控制器，处理用户注册、登录等认证相关请求
 * 作者：CodeBuddy
 * 创建时间：2025-08-11
 * 版本：v1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    
    /**
     * 用户注册第一步：发送验证码
     * @param request 包含邮箱和密码的注册请求
     * @return 统一响应格式
     */
    @PostMapping("/register/step1")
    public ApiResponse<Void> registerStep1(@RequestBody UserRegisterRequest request) {
        log.info("收到用户注册第一步请求: {}", request);
        
        // 参数校验
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "邮箱不能为空");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "密码不能为空");
        }
        
        // 检查密码强度
        if (request.getPassword().length() < 8) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "密码长度不能少于8位");
        }
        
        // 检查邮箱是否已被注册
        User existingUser = userService.findUserByEmail(request.getEmail());
        if (existingUser != null) {
            return ApiResponse.error(HttpStatus.CONFLICT.value(), "该邮箱已被注册");
        }
        
        // 发送验证码
        boolean success = userService.registerStep1(request);
        
        if (success) {
            return ApiResponse.success("验证码已发送至您的邮箱，请查收", (Void) null);
        } else {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "验证码发送失败，请稍后重试");
        }
    }
    
    /**
     * 用户注册第二步：验证验证码并完成注册
     * @param request 包含邮箱、密码和验证码的注册验证请求
     * @return 统一响应格式，包含注册成功的用户信息
     */
    @PostMapping("/register/step2")
    public ApiResponse<User> registerStep2(@RequestBody UserRegisterVerifyRequest request) {
        log.info("收到用户注册第二步请求: {}", request);
        
        // 参数校验
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "邮箱不能为空");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "密码不能为空");
        }
        
        if (request.getCode() == null || request.getCode().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "验证码不能为空");
        }
        
        // 验证验证码并完成注册
        User newUser = userService.registerStep2(request);
        
        if (newUser != null) {
            // 注册成功，返回用户信息（不包含密码）
            newUser.setPassword(null);
            return ApiResponse.success("注册成功", newUser);
        } else {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "注册失败，验证码无效或已过期");
        }
    }
}
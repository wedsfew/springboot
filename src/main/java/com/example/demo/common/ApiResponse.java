package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应结构
 * @author CodeBuddy
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * 业务状态码
     */
    private int code;
    
    /**
     * 响应描述信息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 服务器响应时间戳（ISO 8601格式）
     */
    private String timestamp;
    

    /**
     * 创建成功响应（状态码200）
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 200;
        response.message = "操作成功";
        response.data = data;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 创建成功响应（状态码200），自定义消息
     * @param message 成功消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 200;
        response.message = message;
        response.data = data;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 创建资源创建成功响应（状态码201）
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> created(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 201;
        response.message = "创建成功";
        response.data = data;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 创建错误响应
     * @param code 错误状态码
     * @param message 错误消息
     * @return API响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = code;
        response.message = message;
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 创建错误响应，带数据
     * @param code 错误状态码
     * @param message 错误消息
     * @param data 错误详情数据
     * @param <T> 数据类型
     * @return API响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = code;
        response.message = message;
        response.data = data;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 请求参数错误响应（状态码400）
     * @param message 错误消息
     * @return API响应对象
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 400;
        response.message = message;
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 未授权响应（状态码401）
     * @return API响应对象
     */
    public static <T> ApiResponse<T> unauthorized() {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 401;
        response.message = "未授权，请先登录";
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 禁止访问响应（状态码403）
     * @return API响应对象
     */
    public static <T> ApiResponse<T> forbidden() {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 403;
        response.message = "无权限访问该资源";
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 资源不存在响应（状态码404）
     * @param resourceName 资源名称
     * @return API响应对象
     */
    public static <T> ApiResponse<T> notFound(String resourceName) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 404;
        response.message = resourceName + "不存在";
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 资源冲突响应（状态码409）
     * @param message 冲突描述
     * @return API响应对象
     */
    public static <T> ApiResponse<T> conflict(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 409;
        response.message = message;
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 服务器错误响应（状态码500）
     * @return API响应对象
     */
    public static <T> ApiResponse<T> serverError() {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 500;
        response.message = "服务器内部错误";
        response.data = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }
    
    /**
     * 获取当前时间戳，ISO 8601格式
     * @return 格式化的时间字符串
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
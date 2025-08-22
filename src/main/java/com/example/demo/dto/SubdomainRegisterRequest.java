package com.example.demo.dto;

import lombok.Data;

/**
 * 文件名：SubdomainRegisterRequest.java
 * 功能：用户注册三级域名请求数据传输对象
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
@Data
public class SubdomainRegisterRequest {
    
    /**
     * 三级域名前缀（如test，将注册为test.cblog.eu）
     */
    private String subDomain;
    
    /**
     * 主域名（可选，默认为cblog.eu）
     */
    private String domain;
    
    /**
     * 记录值（IP地址，如8.8.8.8）
     */
    private String value;
    
    /**
     * TTL值（可选，默认600）
     */
    private Long ttl;
}
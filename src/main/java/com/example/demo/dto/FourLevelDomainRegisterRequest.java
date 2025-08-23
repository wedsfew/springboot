package com.example.demo.dto;

import lombok.Data;

/**
 * 文件名：FourLevelDomainRegisterRequest.java
 * 功能：用户注册四级域名请求数据传输对象
 * 作者：CodeBuddy
 * 创建时间：2025-08-23
 * 版本：v1.0.0
 */
@Data
public class FourLevelDomainRegisterRequest {
    
    /**
     * 主域名（可选，默认为cblog.eu）
     */
    private String domain;
    
    /**
     * 记录类型（如A、CNAME等）
     */
    private String recordType;
    
    /**
     * 记录值（IP地址，如8.8.8.8）
     */
    private String value;
    
    /**
     * 记录线路（可选，默认为"默认"）
     */
    private String recordLine;
    
    /**
     * 三级域名前缀（如bb，将与threeDomain组合成aa.bb.cblog.eu）
     */
    private String subDomain;
    
    /**
     * 四级域名前缀（如aa，将与subDomain组合成aa.bb.cblog.eu）
     */
    private String threeDomain;
    
    /**
     * TTL值（可选，默认600）
     */
    private Long ttl;
}
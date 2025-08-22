package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 文件名：UserSubdomain.java
 * 功能：用户三级域名实体类
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
@Data
public class UserSubdomain {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 子域名前缀
     */
    private String subdomain;
    
    /**
     * 主域名
     */
    private String domain;
    
    /**
     * DNSPod记录ID
     */
    private Long recordId;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * TTL值
     */
    private Integer ttl;
    
    /**
     * 状态：ACTIVE-活跃，INACTIVE-不活跃，DELETED-已删除
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
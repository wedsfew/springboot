package com.example.demo.dto;

import lombok.Data;

/**
 * 文件名：RecordRequest.java
 * 功能：DNSPod记录请求DTO，用于接收JSON格式的请求
 * 作者：CodeBuddy
 * 创建时间：2025-08-19
 * 版本：v1.0.0
 */
@Data
public class RecordRequest {
    private String domain;        // 域名（必填）
    private String recordType;    // 记录类型（必填，如A、CNAME等）
    private String value;         // 记录值（必填，如IP地址）
    private String recordLine;    // 记录线路（可选，默认"默认"）
    private String subDomain;     // 主机记录（可选，如www）
    private Long ttl;             // TTL值（可选，默认600）
    private Long mx;              // MX优先级（可选）
    private Long weight;          // 权重（可选）
    private String status;        // 记录状态（可选，默认ENABLE）
    private String remark;        // 备注（可选）
    private Long recordId;        // 记录ID（修改和删除时必填）
    private Long domainId;        // 域名ID（可选）
}
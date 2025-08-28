package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DNS解析记录响应DTO
 */
@Data
public class DnsRecordResponse {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * DNSPod记录ID
     */
    private Long recordId;
    
    /**
     * 主机记录（如www、mail等，@表示主域名）
     */
    private String name;
    
    /**
     * 记录类型（A、CNAME、MX、TXT等）
     */
    private String type;
    
    /**
     * 记录值（如IP地址、域名等）
     */
    private String value;
    
    /**
     * 记录线路
     */
    private String line;
    
    /**
     * TTL值（生存时间）
     */
    private Integer ttl;
    
    /**
     * MX优先级（仅MX记录需要）
     */
    private Integer mx;
    
    /**
     * 权重（负载均衡）
     */
    private Integer weight;
    
    /**
     * 记录状态（ENABLE或DISABLE）
     */
    private String status;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 监控状态
     */
    private String monitorStatus;
    
    /**
     * 同步状态
     */
    private String syncStatus;
    
    /**
     * DNSPod更新时间
     */
    private LocalDateTime updatedOn;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
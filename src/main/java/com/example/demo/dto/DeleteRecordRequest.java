package com.example.demo.dto;

import lombok.Data;

/**
 * 文件名：DeleteRecordRequest.java
 * 功能：DNSPod删除记录请求DTO
 * 作者：CodeBuddy
 * 创建时间：2025-01-16
 * 版本：v1.0.0
 */
@Data
public class DeleteRecordRequest {
    
    /**
     * 域名
     * 示例值：dnspod.cn
     */
    private String domain;
    
    /**
     * 记录 ID
     * 可以通过接口DescribeRecordList查到所有的解析记录列表以及对应的RecordId
     * 示例值：162
     */
    private Long recordId;
    
    /**
     * 域名 ID（可选）
     * 参数 DomainId 优先级比参数 Domain 高，如果传递参数 DomainId 将忽略参数 Domain
     * 可以通过接口DescribeDomainList查到所有的Domain以及DomainId
     * 示例值：1923
     */
    private Long domainId;
}
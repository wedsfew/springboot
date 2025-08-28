package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 获取DNS解析记录请求DTO
 */
@Data
public class GetDnsRecordsRequest {
    
    @NotBlank(message = "域名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.[a-zA-Z]{2,}$", 
             message = "请输入有效的3级域名格式，如：subdomain.domain.com")
    private String fullDomain;
    
    /**
     * 记录类型过滤（可选）
     * 如：A、CNAME、MX、TXT等
     */
    private String type;
    
    /**
     * 记录状态过滤（可选）
     * ENABLE 或 DISABLE
     */
    private String status;
    
    /**
     * 页码（可选，默认为1）
     */
    private Integer page = 1;
    
    /**
     * 每页大小（可选，默认为20）
     */
    private Integer size = 20;
}
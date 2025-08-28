package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 删除DNS解析记录请求DTO
 * 
 * @author CodeBuddy
 * @since 2025-08-28
 */
public class DeleteDnsRecordRequest {
    
    /**
     * 要删除的DNS解析记录ID
     */
    @NotNull(message = "记录ID不能为空")
    @Positive(message = "记录ID必须是大于0的整数")
    private Long id;
    
    public DeleteDnsRecordRequest() {}
    
    public DeleteDnsRecordRequest(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "DeleteDnsRecordRequest{" +
                "id=" + id +
                '}';
    }
}
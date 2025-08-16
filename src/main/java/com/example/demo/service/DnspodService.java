package com.example.demo.service;

import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;

/**
 * 文件名：DnspodService.java
 * 功能：腾讯云DNSPod服务接口，提供域名解析记录查询功能
 * 作者：CodeBuddy
 * 创建时间：2025-08-16
 * 版本：v1.0.0
 */
public interface DnspodService {
    
    /**
     * 获取域名的解析记录筛选列表
     * 
     * @param domain 域名，如 example.com
     * @param remark 备注信息（可选）
     * @param subDomain 子域名（可选）
     * @param recordType 记录类型（可选）
     * @param limit 限制数量（可选）
     * @param offset 偏移量（可选）
     * @return 解析记录列表响应
     */
    DescribeRecordFilterListResponse getRecordFilterList(String domain, String remark, 
                                                        String subDomain, String recordType,
                                                        Integer limit, Integer offset);
}
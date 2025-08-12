package com.example.demo.service;

import java.util.List;
import java.util.Map;

/**
 * 文件名：DnspodService.java
 * 功能：DNSPod服务接口，定义DNSPod相关操作
 * 作者：CodeBuddy
 * 创建时间：2025-08-12
 * 版本：v1.0.0
 */
public interface DnspodService {
    
    /**
     * 获取域名列表
     * @return 域名列表
     */
    List<Map<String, Object>> getDomainList();
    
    /**
     * 获取域名记录列表
     * @param domain 域名
     * @return 记录列表
     */
    List<Map<String, Object>> getRecordList(String domain);
    
    /**
     * 创建域名记录
     * @param domain 域名
     * @param subDomain 子域名
     * @param recordType 记录类型（A, CNAME, MX等）
     * @param recordLine 记录线路
     * @param value 记录值
     * @param ttl TTL值（生存时间）
     * @return 创建结果
     */
    Map<String, Object> createRecord(String domain, String subDomain, String recordType, 
                                    String recordLine, String value, Integer ttl);
    
    /**
     * 修改域名记录
     * @param domain 域名
     * @param recordId 记录ID
     * @param subDomain 子域名
     * @param recordType 记录类型
     * @param recordLine 记录线路
     * @param value 记录值
     * @param ttl TTL值
     * @return 修改结果
     */
    Map<String, Object> modifyRecord(String domain, String recordId, String subDomain, 
                                    String recordType, String recordLine, String value, Integer ttl);
    
    /**
     * 删除域名记录
     * @param domain 域名
     * @param recordId 记录ID
     * @return 删除结果
     */
    Map<String, Object> deleteRecord(String domain, String recordId);
}
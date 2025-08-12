package com.example.demo.service;

import java.util.List;
import java.util.Map;

/**
 * 文件名：DnspodService.java
 * 功能：DNSPod服务接口，定义DNSPod相关操作
 * 作者：CodeBuddy
 * 创建时间：2025-08-12
 * 版本：v1.0.0
 * 更新时间：2025-08-12
 */
public interface DnspodService {
    
    /**
     * 获取域名列表
     * @return 域名列表
     */
    List<Map<String, Object>> getDomainList();
    
    /**
     * 获取域名列表（带分页）
     * @param offset 偏移量（可选，默认0）
     * @param limit 限制数量（可选，默认20）
     * @param keyword 关键字（可选，用于搜索域名）
     * @param groupId 分组ID（可选）
     * @return 域名列表及分页信息
     */
    Map<String, Object> getDomainListWithPagination(Integer offset, Integer limit, String keyword, Integer groupId);
    
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
    
    /**
     * 创建记录分组
     * @param domain 域名
     * @param groupName 分组名称
     * @param domainId 域名ID（可选，如果提供则优先使用）
     * @return 创建结果，包含新增的分组ID
     */
    Map<String, Object> createRecordGroup(String domain, String groupName, Long domainId);
    
    /**
     * 获取域名解析记录列表
     * @param domain 域名
     * @param domainId 域名ID（可选，如果提供则优先使用）
     * @param subdomain 主机头（可选，如果提供则只返回此主机头对应的记录）
     * @param recordType 记录类型（可选，如A，CNAME，NS等）
     * @param recordLine 线路名称（可选）
     * @param recordLineId 线路ID（可选，优先于recordLine）
     * @param groupId 分组ID（可选）
     * @param keyword 关键字（可选，用于搜索主机头和记录值）
     * @param sortField 排序字段（可选）
     * @param sortType 排序方式（可选，ASC或DESC）
     * @param offset 偏移量（可选，默认0）
     * @param limit 限制数量（可选，默认100，最大3000）
     * @return 记录列表及统计信息
     */
    Map<String, Object> getRecordList(String domain, Long domainId, String subdomain, 
                                     String recordType, String recordLine, String recordLineId,
                                     Integer groupId, String keyword, String sortField, 
                                     String sortType, Integer offset, Integer limit);
}

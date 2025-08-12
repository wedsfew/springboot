package com.example.demo.service.impl;

import com.example.demo.service.DnspodService;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名：DnspodServiceImpl.java
 * 功能：DNSPod服务实现类，实现DNSPod相关操作
 * 作者：CodeBuddy
 * 创建时间：2025-08-12
 * 版本：v1.0.0
 * 更新时间：2025-08-12
 */
@Service
public class DnspodServiceImpl implements DnspodService {

    private static final Logger logger = LoggerFactory.getLogger(DnspodServiceImpl.class);
    
    @Autowired
    private DnspodClient dnspodClient;

    @Override
    public List<Map<String, Object>> getDomainList() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            // 实例化一个请求对象
            DescribeDomainListRequest req = new DescribeDomainListRequest();
            
            // 返回的resp是一个DescribeDomainListResponse的实例
            DescribeDomainListResponse resp = dnspodClient.DescribeDomainList(req);
            
            // 处理返回结果
            for (DomainListItem domain : resp.getDomainList()) {
                Map<String, Object> domainMap = new HashMap<>();
                domainMap.put("domainId", domain.getDomainId());
                domainMap.put("name", domain.getName());
                domainMap.put("status", domain.getStatus());
                domainMap.put("recordCount", domain.getRecordCount());
                result.add(domainMap);
            }
        } catch (TencentCloudSDKException e) {
            logger.error("获取域名列表失败: {}", e.getMessage());
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getDomainListWithPagination(Integer offset, Integer limit, String keyword, Integer groupId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> domainList = new ArrayList<>();
        
        try {
            // 实例化一个请求对象
            DescribeDomainListRequest req = new DescribeDomainListRequest();
            
            // 设置分页参数
            if (offset != null) {
                req.setOffset(offset.longValue());
            } else {
                req.setOffset(0L); // 默认从0开始
            }
            
            if (limit != null) {
                req.setLimit(limit.longValue());
            } else {
                req.setLimit(20L); // 默认每页20条
            }
            
            // 设置可选参数
            if (keyword != null && !keyword.isEmpty()) {
                req.setKeyword(keyword);
            }
            
            if (groupId != null) {
                req.setGroupId(groupId.longValue());
            }
            
            // 调用API获取域名列表
            DescribeDomainListResponse resp = dnspodClient.DescribeDomainList(req);
            
            // 处理返回结果 - 域名列表
            for (DomainListItem domain : resp.getDomainList()) {
                Map<String, Object> domainMap = new HashMap<>();
                domainMap.put("domainId", domain.getDomainId());
                domainMap.put("name", domain.getName());
                domainMap.put("status", domain.getStatus());
                domainMap.put("recordCount", domain.getRecordCount());
                domainMap.put("grade", domain.getGrade());
                domainMap.put("groupId", domain.getGroupId());
                // 移除不存在的方法调用
                domainMap.put("remark", domain.getRemark());
                domainMap.put("createdOn", domain.getCreatedOn());
                domainMap.put("updatedOn", domain.getUpdatedOn());
                
                domainList.add(domainMap);
            }
            
            // 处理返回结果 - 分页信息
            Map<String, Object> paginationInfo = new HashMap<>();
            paginationInfo.put("offset", offset != null ? offset : 0);
            paginationInfo.put("limit", limit != null ? limit : 20);
            
            // 直接使用DomainCountInfo对象，不调用可能不存在的方法
            Map<String, Object> countInfo = new HashMap<>();
            countInfo.put("domain_total", resp.getDomainCountInfo().getDomainTotal());
            countInfo.put("all_total", resp.getDomainCountInfo().getAllTotal());
            countInfo.put("mine_total", resp.getDomainCountInfo().getMineTotal());
            
            // 组装最终结果
            result.put("domains", domainList);
            result.put("info", countInfo);
            result.put("success", true);
            result.put("message", "获取域名列表成功");
            result.put("requestId", resp.getRequestId());
            
            // 记录日志
            logger.info("获取域名列表成功: offset={}, limit={}, totalCount={}", 
                       offset, limit, resp.getDomainCountInfo().getDomainTotal());
        } catch (TencentCloudSDKException e) {
            logger.error("获取域名列表失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "获取域名列表失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getRecordList(String domain) {
        return getRecordList(domain, null, null, null, null, null, null, null, null, null, 0, 100)
            .get("recordList") != null ? (List<Map<String, Object>>) getRecordList(domain, null, null, null, null, null, null, null, null, null, 0, 100).get("recordList") : new ArrayList<>();
    }

    @Override
    public Map<String, Object> createRecord(String domain, String subDomain, String recordType, 
                                          String recordLine, String value, Integer ttl) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 实例化一个请求对象
            CreateRecordRequest req = new CreateRecordRequest();
            req.setDomain(domain);
            req.setSubDomain(subDomain);
            req.setRecordType(recordType);
            req.setRecordLine(recordLine);
            req.setValue(value);
            
            // 设置TTL值，如果未提供则使用默认值600
            if (ttl != null) {
                // 确保TTL值在有效范围内
                if (ttl < 600) {
                    ttl = 600;
                } else if (ttl > 86400) {
                    ttl = 86400;
                }
                req.setTTL(ttl.longValue());
            } else {
                req.setTTL(600L); // 默认TTL为600秒
            }
            
            // 返回的resp是一个CreateRecordResponse的实例
            CreateRecordResponse resp = dnspodClient.CreateRecord(req);
            
            // 处理返回结果
            result.put("recordId", resp.getRecordId());
            result.put("success", true);
            result.put("message", "创建记录成功");
        } catch (TencentCloudSDKException e) {
            logger.error("创建记录失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "创建记录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> modifyRecord(String domain, String recordId, String subDomain, 
                                          String recordType, String recordLine, String value, Integer ttl) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 实例化一个请求对象
            ModifyRecordRequest req = new ModifyRecordRequest();
            req.setDomain(domain);
            req.setRecordId(Long.parseLong(recordId));
            req.setSubDomain(subDomain);
            req.setRecordType(recordType);
            req.setRecordLine(recordLine);
            req.setValue(value);
            
            // 设置TTL值，如果未提供则使用默认值600
            if (ttl != null) {
                // 确保TTL值在有效范围内
                if (ttl < 600) {
                    ttl = 600;
                } else if (ttl > 86400) {
                    ttl = 86400;
                }
                req.setTTL(ttl.longValue());
            } else {
                req.setTTL(600L); // 默认TTL为600秒
            }
            
            // 返回的resp是一个ModifyRecordResponse的实例
            ModifyRecordResponse resp = dnspodClient.ModifyRecord(req);
            
            // 处理返回结果
            result.put("recordId", resp.getRecordId());
            result.put("success", true);
            result.put("message", "修改记录成功");
        } catch (TencentCloudSDKException e) {
            logger.error("修改记录失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "修改记录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteRecord(String domain, String recordId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 实例化一个请求对象
            DeleteRecordRequest req = new DeleteRecordRequest();
            req.setDomain(domain);
            req.setRecordId(Long.parseLong(recordId));
            
            // 返回的resp是一个DeleteRecordResponse的实例
            dnspodClient.DeleteRecord(req);
            
            // 处理返回结果
            result.put("success", true);
            result.put("message", "删除记录成功");
        } catch (TencentCloudSDKException e) {
            logger.error("删除记录失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "删除记录失败: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public Map<String, Object> createRecordGroup(String domain, String groupName, Long domainId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 实例化一个请求对象
            CreateRecordGroupRequest req = new CreateRecordGroupRequest();
            
            // 设置域名，如果提供了domainId则优先使用
            if (domainId != null) {
                req.setDomainId(domainId);
            } else {
                req.setDomain(domain);
            }
            
            // 设置分组名称
            req.setGroupName(groupName);
            
            // 调用API创建记录分组
            CreateRecordGroupResponse resp = dnspodClient.CreateRecordGroup(req);
            
            // 处理返回结果
            result.put("groupId", resp.getGroupId());
            result.put("success", true);
            result.put("message", "创建记录分组成功");
            result.put("requestId", resp.getRequestId());
            
            // 记录日志
            logger.info("创建记录分组成功: domain={}, groupName={}, groupId={}", 
                       domain, groupName, resp.getGroupId());
        } catch (TencentCloudSDKException e) {
            logger.error("创建记录分组失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "创建记录分组失败: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getRecordList(String domain, Long domainId, String subdomain, 
                                           String recordType, String recordLine, String recordLineId,
                                           Integer groupId, String keyword, String sortField, 
                                           String sortType, Integer offset, Integer limit) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> recordList = new ArrayList<>();
        
        try {
            // 实例化一个请求对象
            DescribeRecordListRequest req = new DescribeRecordListRequest();
            
            // 设置域名，如果提供了domainId则优先使用
            if (domainId != null) {
                req.setDomainId(domainId);
            } else {
                req.setDomain(domain);
            }
            
            // 设置可选参数
            if (subdomain != null) {
                req.setSubdomain(subdomain);
            }
            
            if (recordType != null) {
                req.setRecordType(recordType);
            }
            
            if (recordLine != null) {
                req.setRecordLine(recordLine);
            }
            
            if (recordLineId != null) {
                req.setRecordLineId(recordLineId);
            }
            
            if (groupId != null) {
                // 修复：将Integer类型的groupId转换为Long类型
                req.setGroupId(groupId.longValue());
            }
            
            if (keyword != null) {
                req.setKeyword(keyword);
            }
            
            if (sortField != null) {
                req.setSortField(sortField);
            }
            
            if (sortType != null) {
                req.setSortType(sortType);
            }
            
            if (offset != null) {
                req.setOffset(offset.longValue());
            }
            
            if (limit != null) {
                req.setLimit(limit.longValue());
            } else {
                // 默认限制为100条
                req.setLimit(100L);
            }
            
            // 调用API获取记录列表
            DescribeRecordListResponse resp = dnspodClient.DescribeRecordList(req);
            
            // 处理返回结果 - 记录列表
            for (RecordListItem record : resp.getRecordList()) {
                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("recordId", record.getRecordId());
                recordMap.put("value", record.getValue());
                recordMap.put("status", record.getStatus());
                recordMap.put("updatedOn", record.getUpdatedOn());
                recordMap.put("name", record.getName());
                recordMap.put("line", record.getLine());
                recordMap.put("lineId", record.getLineId());
                recordMap.put("type", record.getType());
                recordMap.put("weight", record.getWeight());
                recordMap.put("monitorStatus", record.getMonitorStatus());
                recordMap.put("remark", record.getRemark());
                recordMap.put("ttl", record.getTTL());
                recordMap.put("mx", record.getMX());
                recordMap.put("defaultNS", record.getDefaultNS());
                
                recordList.add(recordMap);
            }
            
            // 处理返回结果 - 统计信息
            Map<String, Object> countInfo = new HashMap<>();
            countInfo.put("subdomainCount", resp.getRecordCountInfo().getSubdomainCount());
            countInfo.put("totalCount", resp.getRecordCountInfo().getTotalCount());
            countInfo.put("listCount", resp.getRecordCountInfo().getListCount());
            
            // 组装最终结果
            result.put("recordList", recordList);
            result.put("recordCountInfo", countInfo);
            result.put("success", true);
            result.put("message", "获取记录列表成功");
            result.put("requestId", resp.getRequestId());
            
            // 记录日志
            logger.info("获取记录列表成功: domain={}, totalCount={}", 
                       domain, resp.getRecordCountInfo().getTotalCount());
        } catch (TencentCloudSDKException e) {
            logger.error("获取记录列表失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "获取记录列表失败: " + e.getMessage());
        }
        
        return result;
    }
}
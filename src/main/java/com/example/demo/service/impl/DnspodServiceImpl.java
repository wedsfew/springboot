package com.example.demo.service.impl;

import com.example.demo.service.DnspodService;
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
    public List<Map<String, Object>> getRecordList(String domain) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            // 实例化一个请求对象
            DescribeRecordListRequest req = new DescribeRecordListRequest();
            req.setDomain(domain);
            
            // 返回的resp是一个DescribeRecordListResponse的实例
            DescribeRecordListResponse resp = dnspodClient.DescribeRecordList(req);
            
            // 处理返回结果
            for (RecordListItem record : resp.getRecordList()) {
                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("recordId", record.getRecordId());
                recordMap.put("subDomain", record.getName());
                recordMap.put("recordType", record.getType());
                recordMap.put("recordLine", record.getLine());
                recordMap.put("value", record.getValue());
                recordMap.put("ttl", record.getTTL());
                recordMap.put("status", record.getStatus());
                result.add(recordMap);
            }
        } catch (TencentCloudSDKException e) {
            logger.error("获取记录列表失败: {}", e.getMessage());
        }
        return result;
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
            if (ttl != null) {
                req.setTTL(ttl.longValue());
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
            if (ttl != null) {
                req.setTTL(ttl.longValue());
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
}
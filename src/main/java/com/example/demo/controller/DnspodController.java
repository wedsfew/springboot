package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.service.DnspodService;
import com.tencentcloudapi.dnspod.v20210323.models.CreateRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DeleteRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文件名：DnspodController.java
 * 功能：腾讯云DNSPod控制器，提供域名解析记录查询API接口
 * 作者：CodeBuddy
 * 创建时间：2025-08-16
 * 版本：v1.0.0
 */
@RestController
@RequestMapping("/api/dnspod")
public class DnspodController {

    @Autowired
    private DnspodService dnspodService;

    /**
     * 获取域名的解析记录筛选列表
     * 
     * @param domain 域名（必填）
     * @param remark 备注信息（可选）
     * @param subDomain 子域名（可选）
     * @param recordType 记录类型（可选）
     * @param limit 限制数量（可选，默认100）
     * @param offset 偏移量（可选，默认0）
     * @return 解析记录列表
     */
    @GetMapping("/records")
    public ApiResponse<DescribeRecordFilterListResponse> getRecordFilterList(
            @RequestParam String domain,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) String subDomain,
            @RequestParam(required = false) String recordType,
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {
        
        try {
            DescribeRecordFilterListResponse response = dnspodService.getRecordFilterList(
                domain, remark, subDomain, recordType, limit, offset);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名解析记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定域名的所有解析记录（简化版）
     * 
     * @param domain 域名
     * @return 解析记录列表
     */
    @GetMapping("/records/{domain}")
    public ApiResponse<DescribeRecordFilterListResponse> getRecordsByDomain(@PathVariable String domain) {
        try {
            DescribeRecordFilterListResponse response = dnspodService.getRecordFilterList(
                domain, null, null, null, 100, 0);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名解析记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加域名解析记录
     * 
     * @param domain 域名（必填）
     * @param recordType 记录类型（必填，如A、CNAME等）
     * @param value 记录值（必填，如IP地址）
     * @param recordLine 记录线路（可选，默认"默认"）
     * @param subDomain 主机记录（可选，如www）
     * @param ttl TTL值（可选，默认600）
     * @param mx MX优先级（可选）
     * @param weight 权重（可选）
     * @param status 记录状态（可选，默认ENABLE）
     * @param remark 备注（可选）
     * @return ApiResponse<CreateRecordResponse> 统一响应格式
     */
    @PostMapping("/records")
    public ApiResponse<CreateRecordResponse> createRecord(
            @RequestParam String domain,
            @RequestParam String recordType,
            @RequestParam String value,
            @RequestParam(required = false, defaultValue = "默认") String recordLine,
            @RequestParam(required = false) String subDomain,
            @RequestParam(required = false) Long ttl,
            @RequestParam(required = false) Long mx,
            @RequestParam(required = false) Long weight,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String remark) {
        
        try {
            CreateRecordResponse response = dnspodService.createRecord(
                domain, recordType, recordLine, value, subDomain, 
                ttl, mx, weight, status, remark
            );
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建域名解析记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除域名解析记录
     * 
     * @param domain 域名（必填）
     * @param recordId 记录ID（必填）
     * @param domainId 域名ID（可选）
     * @return ApiResponse<DeleteRecordResponse> 统一响应格式
     */
    @DeleteMapping("/records")
    public ApiResponse<DeleteRecordResponse> deleteRecord(
            @RequestParam String domain,
            @RequestParam Long recordId,
            @RequestParam(required = false) Long domainId) {
        
        try {
            DeleteRecordResponse response = dnspodService.deleteRecord(domain, recordId, domainId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "删除域名解析记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改域名解析记录
     * 
     * @param domain 域名（必填）
     * @param recordId 记录ID（必填）
     * @param recordType 记录类型（必填，如A、CNAME等）
     * @param value 记录值（必填，如IP地址）
     * @param recordLine 记录线路（可选，默认"默认"）
     * @param subDomain 主机记录（可选，如www）
     * @param domainId 域名ID（可选）
     * @param ttl TTL值（可选）
     * @param mx MX优先级（可选）
     * @param weight 权重（可选）
     * @param status 记录状态（可选）
     * @param remark 备注（可选）
     * @return ApiResponse<ModifyRecordResponse> 统一响应格式
     */
    @PostMapping("/records/modify")
    public ApiResponse<ModifyRecordResponse> modifyRecord(
            @RequestParam String domain,
            @RequestParam Long recordId,
            @RequestParam String recordType,
            @RequestParam String value,
            @RequestParam(required = false, defaultValue = "默认") String recordLine,
            @RequestParam(required = false) String subDomain,
            @RequestParam(required = false) Long domainId,
            @RequestParam(required = false) Long ttl,
            @RequestParam(required = false) Long mx,
            @RequestParam(required = false) Long weight,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String remark) {
        
        try {
            ModifyRecordResponse response = dnspodService.modifyRecord(
                domain, recordId, recordType, recordLine, value, subDomain,
                domainId, ttl, mx, weight, status, remark
            );
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "修改域名解析记录失败: " + e.getMessage());
        }
    }
}
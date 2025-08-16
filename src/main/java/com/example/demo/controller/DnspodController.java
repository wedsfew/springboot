package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.service.DnspodService;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;
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
}
package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.mapper.DomainMapper;
import com.example.demo.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件名：DomainController.java
 * 功能：域名控制器，提供域名相关API接口
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
@RestController
@RequestMapping("/api/domains")
@CrossOrigin(origins = "*")
public class DomainController {
    
    @Autowired
    private DomainMapper domainMapper;
    
    @Autowired
    private DomainService domainService;
    
    /**
     * 获取可用的域名后缀列表
     * 
     * @return 统一响应格式，包含域名后缀列表
     */
    @GetMapping("/suffixes")
    public ApiResponse<List<String>> getDomainSuffixes() {
        try {
            List<String> suffixes = domainMapper.findAvailableDomainSuffixes();
            return ApiResponse.success("获取域名后缀成功", suffixes);
        } catch (Exception e) {
            return ApiResponse.error(500, "系统繁忙，请稍后重试");
        }
    }
    
    /**
     * 从DNSPod获取域名列表并同步到本地数据库
     * 
     * @return 统一响应格式，包含同步结果
     */
    @PostMapping("/sync")
    public ApiResponse<?> syncDomains() {
        try {
            int count = domainService.syncDomainsFromDnspod();
            return ApiResponse.success("操作成功", "成功同步 " + count + " 个域名");
        } catch (Exception e) {
            return ApiResponse.error(500, "同步域名列表失败: " + e.getMessage());
        }
    }
}
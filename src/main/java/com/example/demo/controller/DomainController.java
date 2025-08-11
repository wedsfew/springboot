// 文件名：DomainController.java
// 功能：域名管理控制器，提供域名相关的API接口
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：遵循API开发规则，使用统一响应格式

package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Domain;
import com.example.demo.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 域名管理控制器
 * 提供域名相关的API接口，包括获取域名列表、同步域名数据等功能
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/domains")
public class DomainController {

    @Autowired
    private DomainService domainService;

    /**
     * 获取本地数据库中的所有域名信息
     * @return 域名信息列表
     */
    @GetMapping
    public ApiResponse<List<Domain>> getAllDomains() {
        try {
            log.info("获取所有域名信息");
            List<Domain> domains = domainService.getAllDomains();
            return ApiResponse.success("获取域名列表成功", domains);
        } catch (Exception e) {
            log.error("获取域名列表失败", e);
            return ApiResponse.error(500, "获取域名列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据域名ID获取域名信息
     * @param domainId 腾讯云域名ID
     * @return 域名信息
     */
    @GetMapping("/{domainId}")
    public ApiResponse<Domain> getDomainById(@PathVariable Long domainId) {
        try {
            log.info("根据域名ID获取域名信息: {}", domainId);
            Domain domain = domainService.getDomainByDomainId(domainId);
            
            if (domain == null) {
                return ApiResponse.error(404, "域名不存在");
            }
            
            return ApiResponse.success("获取域名信息成功", domain);
        } catch (Exception e) {
            log.error("获取域名信息失败", e);
            return ApiResponse.error(500, "获取域名信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据域名名称获取域名信息
     * @param domainName 域名名称
     * @return 域名信息
     */
    @GetMapping("/name/{domainName}")
    public ApiResponse<Domain> getDomainByName(@PathVariable String domainName) {
        try {
            log.info("根据域名名称获取域名信息: {}", domainName);
            Domain domain = domainService.getDomainByDomainName(domainName);
            
            if (domain == null) {
                return ApiResponse.error(404, "域名不存在");
            }
            
            return ApiResponse.success("获取域名信息成功", domain);
        } catch (Exception e) {
            log.error("获取域名信息失败", e);
            return ApiResponse.error(500, "获取域名信息失败: " + e.getMessage());
        }
    }

    /**
     * 从腾讯云DNSPod获取域名列表（不保存到数据库）
     * @return 域名信息列表
     */
    @GetMapping("/cloud")
    public ApiResponse<List<Domain>> getDomainsFromCloud() {
        try {
            log.info("从腾讯云DNSPod获取域名列表");
            List<Domain> domains = domainService.getDomainListFromDNSPod();
            return ApiResponse.success("从腾讯云获取域名列表成功", domains);
        } catch (Exception e) {
            log.error("从腾讯云获取域名列表失败", e);
            return ApiResponse.error(500, "从腾讯云获取域名列表失败: " + e.getMessage());
        }
    }

    /**
     * 同步域名数据：从腾讯云DNSPod获取最新域名列表并更新到数据库
     * @return 同步结果信息
     */
    @PostMapping("/sync")
    public ApiResponse<String> syncDomains() {
        try {
            log.info("开始同步域名数据");
            String result = domainService.syncDomainsFromDNSPod();
            return ApiResponse.success("域名数据同步成功", result);
        } catch (Exception e) {
            log.error("域名数据同步失败", e);
            return ApiResponse.error(500, "域名数据同步失败: " + e.getMessage());
        }
    }

    /**
     * 删除域名信息
     * @param domainId 腾讯云域名ID
     * @return 删除结果
     */
    @DeleteMapping("/{domainId}")
    public ApiResponse<String> deleteDomain(@PathVariable Long domainId) {
        try {
            log.info("删除域名信息: {}", domainId);
            boolean success = domainService.deleteDomain(domainId);
            
            if (success) {
                return ApiResponse.success("域名删除成功", "删除成功");
            } else {
                return ApiResponse.error(404, "域名不存在或删除失败");
            }
        } catch (Exception e) {
            log.error("删除域名失败", e);
            return ApiResponse.error(500, "删除域名失败: " + e.getMessage());
        }
    }
}
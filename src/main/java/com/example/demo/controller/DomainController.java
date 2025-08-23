package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Domain;
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
    
    /**
     * 获取所有域名
     * 
     * @return 统一响应格式，包含域名列表
     */
    @GetMapping
    public ApiResponse<List<Domain>> getAllDomains() {
        try {
            List<Domain> domains = domainService.getAllDomains();
            return ApiResponse.success("操作成功", domains);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据DNSPod域名ID获取域名
     * 
     * @param domainId DNSPod域名ID
     * @return 统一响应格式，包含域名信息
     */
    @GetMapping("/dnspod/{domainId}")
    public ApiResponse<Domain> getDomainByDomainId(@PathVariable Long domainId) {
        try {
            Domain domain = domainService.getDomainByDomainId(domainId);
            if (domain == null) {
                return ApiResponse.error(404, "域名不存在");
            }
            return ApiResponse.success("操作成功", domain);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据域名名称获取域名
     * 
     * @param name 域名名称
     * @return 统一响应格式，包含域名信息
     */
    @GetMapping("/name/{name}")
    public ApiResponse<Domain> getDomainByName(@PathVariable String name) {
        try {
            Domain domain = domainService.getDomainByName(name);
            if (domain == null) {
                return ApiResponse.error(404, "域名不存在");
            }
            return ApiResponse.success("操作成功", domain);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据状态获取域名列表
     * 
     * @param status 域名状态
     * @return 统一响应格式，包含域名列表
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<Domain>> getDomainsByStatus(@PathVariable String status) {
        try {
            List<Domain> domains = domainService.getDomainsByStatus(status);
            return ApiResponse.success("操作成功", domains);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据分组ID获取域名列表
     * 
     * @param groupId 分组ID
     * @return 统一响应格式，包含域名列表
     */
    @GetMapping("/group/{groupId}")
    public ApiResponse<List<Domain>> getDomainsByGroupId(@PathVariable Integer groupId) {
        try {
            List<Domain> domains = domainService.getDomainsByGroupId(groupId);
            return ApiResponse.success("操作成功", domains);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索域名
     * 
     * @param keyword 搜索关键字
     * @return 统一响应格式，包含域名列表
     */
    @GetMapping("/search")
    public ApiResponse<List<Domain>> searchDomains(@RequestParam String keyword) {
        try {
            List<Domain> domains = domainService.searchDomains(keyword);
            return ApiResponse.success("操作成功", domains);
        } catch (Exception e) {
            return ApiResponse.error(500, "搜索域名失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页获取域名列表
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 统一响应格式，包含域名列表
     */
    @GetMapping("/page")
    public ApiResponse<List<Domain>> getDomainsWithPagination(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer limit) {
        try {
            List<Domain> domains = domainService.getDomainsWithPagination(offset, limit);
            return ApiResponse.success("操作成功", domains);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取域名列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计域名总数
     * 
     * @return 统一响应格式，包含域名总数
     */
    @GetMapping("/count")
    public ApiResponse<Integer> countAllDomains() {
        try {
            int count = domainService.countAllDomains();
            return ApiResponse.success("操作成功", count);
        } catch (Exception e) {
            return ApiResponse.error(500, "统计域名数量失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据状态统计域名数量
     * 
     * @param status 域名状态
     * @return 统一响应格式，包含域名数量
     */
    @GetMapping("/count/status/{status}")
    public ApiResponse<Integer> countDomainsByStatus(@PathVariable String status) {
        try {
            int count = domainService.countDomainsByStatus(status);
            return ApiResponse.success("操作成功", count);
        } catch (Exception e) {
            return ApiResponse.error(500, "统计域名数量失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存域名信息
     * 
     * @param domain 域名信息
     * @return 统一响应格式，包含保存结果
     */
    @PostMapping
    public ApiResponse<Domain> saveDomain(@RequestBody Domain domain) {
        try {
            Domain savedDomain = domainService.saveDomain(domain);
            return ApiResponse.success("操作成功", savedDomain);
        } catch (Exception e) {
            return ApiResponse.error(500, "保存域名信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新域名信息
     * 
     * @param domain 域名信息
     * @return 统一响应格式，包含更新结果
     */
    @PostMapping("/update")
    public ApiResponse<Domain> updateDomain(@RequestBody Domain domain) {
        try {
            Domain updatedDomain = domainService.updateDomain(domain);
            return ApiResponse.success("操作成功", updatedDomain);
        } catch (Exception e) {
            return ApiResponse.error(500, "更新域名信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除域名信息
     * 
     * @param domainId DNSPod域名ID
     * @return 统一响应格式，包含删除结果
     */
    @PostMapping("/delete/{domainId}")
    public ApiResponse<Boolean> deleteDomain(@PathVariable Long domainId) {
        try {
            boolean result = domainService.deleteDomainByDomainId(domainId);
            if (result) {
                return ApiResponse.success("操作成功", true);
            } else {
                return ApiResponse.error(404, "域名不存在或删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "删除域名信息失败: " + e.getMessage());
        }
    }
}

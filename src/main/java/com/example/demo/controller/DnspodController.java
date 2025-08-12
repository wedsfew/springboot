package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.service.DnspodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文件名：DnspodController.java
 * 功能：DNSPod控制器，提供DNSPod相关API接口
 * 作者：CodeBuddy
 * 创建时间：2025-08-12
 * 版本：v1.0.0
 * 更新时间：2025-08-12
 */
@RestController
@RequestMapping("/api/dnspod")
public class DnspodController {

    @Autowired
    private DnspodService dnspodService;

    /**
     * 获取域名列表
     * @return 域名列表
     */
    @GetMapping("/domains")
    public ApiResponse<List<Map<String, Object>>> getDomainList() {
        List<Map<String, Object>> domains = dnspodService.getDomainList();
        return ApiResponse.success("获取域名列表成功", domains);
    }

    /**
     * 获取域名记录列表
     * @param domain 域名
     * @return 记录列表
     */
    @GetMapping("/domains/{domain}/records")
    public ApiResponse<List<Map<String, Object>>> getRecordList(@PathVariable String domain) {
        List<Map<String, Object>> records = dnspodService.getRecordList(domain);
        return ApiResponse.success("获取记录列表成功", records);
    }
    
    /**
     * 获取域名解析记录列表（高级查询）
     * @param domain 域名
     * @param params 查询参数
     * @return 记录列表及统计信息
     */
    @RequestMapping(value = "/domains/{domain}/records/search", method = RequestMethod.GET)
    public ApiResponse<Map<String, Object>> searchRecordList(
            @PathVariable String domain,
            @RequestParam(required = false) Long domainId,
            @RequestParam(required = false) String subdomain,
            @RequestParam(required = false) String recordType,
            @RequestParam(required = false) String recordLine,
            @RequestParam(required = false) String recordLineId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        
        Map<String, Object> result = dnspodService.getRecordList(
            domain, domainId, subdomain, recordType, recordLine, recordLineId,
            groupId, keyword, sortField, sortType, offset, limit
        );
        
        if ((Boolean) result.get("success")) {
            return ApiResponse.success((String) result.get("message"), result);
        } else {
            return ApiResponse.badRequest((String) result.get("message"));
        }
    }

    /**
     * 创建域名记录
     * @param domain 域名
     * @param params 请求参数
     * @return 创建结果
     */
    @PostMapping("/domains/{domain}/records")
    public ApiResponse<Map<String, Object>> createRecord(@PathVariable String domain, @RequestBody Map<String, Object> params) {
        String subDomain = (String) params.get("subDomain");
        String recordType = (String) params.get("recordType");
        String recordLine = (String) params.get("recordLine");
        String value = (String) params.get("value");
        Integer ttl = (Integer) params.get("ttl");
        
        Map<String, Object> result = dnspodService.createRecord(domain, subDomain, recordType, recordLine, value, ttl);
        
        if ((Boolean) result.get("success")) {
            return ApiResponse.created(result);
        } else {
            return ApiResponse.badRequest((String) result.get("message"));
        }
    }

    /**
     * 修改域名记录
     * @param domain 域名
     * @param recordId 记录ID
     * @param params 请求参数
     * @return 修改结果
     */
    @PutMapping("/domains/{domain}/records/{recordId}")
    public ApiResponse<Map<String, Object>> modifyRecord(@PathVariable String domain, @PathVariable String recordId, 
                                   @RequestBody Map<String, Object> params) {
        String subDomain = (String) params.get("subDomain");
        String recordType = (String) params.get("recordType");
        String recordLine = (String) params.get("recordLine");
        String value = (String) params.get("value");
        Integer ttl = (Integer) params.get("ttl");
        
        Map<String, Object> result = dnspodService.modifyRecord(domain, recordId, subDomain, recordType, recordLine, value, ttl);
        
        if ((Boolean) result.get("success")) {
            return ApiResponse.success((String) result.get("message"), result);
        } else {
            return ApiResponse.badRequest((String) result.get("message"));
        }
    }

    /**
     * 删除域名记录
     * @param domain 域名
     * @param recordId 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/domains/{domain}/records/{recordId}")
    public ApiResponse<Void> deleteRecord(@PathVariable String domain, @PathVariable String recordId) {
        Map<String, Object> result = dnspodService.deleteRecord(domain, recordId);
        
        if ((Boolean) result.get("success")) {
            return ApiResponse.success((String) result.get("message"), null);
        } else {
            return ApiResponse.badRequest((String) result.get("message"));
        }
    }
    
    /**
     * 创建记录分组
     * @param domain 域名
     * @param params 请求参数
     * @return 创建结果
     */
    @PostMapping("/domains/{domain}/groups")
    public ApiResponse<Map<String, Object>> createRecordGroup(@PathVariable String domain, @RequestBody Map<String, Object> params) {
        String groupName = (String) params.get("groupName");
        Long domainId = params.get("domainId") != null ? Long.valueOf(params.get("domainId").toString()) : null;
        
        Map<String, Object> result = dnspodService.createRecordGroup(domain, groupName, domainId);
        
        if ((Boolean) result.get("success")) {
            return ApiResponse.created(result);
        } else {
            return ApiResponse.badRequest((String) result.get("message"));
        }
    }
}
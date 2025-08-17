package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.BusinessException;
import com.example.demo.common.ResourceNotFoundException;
import com.example.demo.dto.DeleteRecordRequest;
import com.example.demo.dto.ModifyRecordRequest;
import com.example.demo.entity.User;
import com.example.demo.service.DnspodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API测试控制器
 * @author CodeBuddy
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class ApiTestController {

    private final DnspodService dnspodService;

    /**
     * 测试成功响应
     * @return 成功响应
     */
    @GetMapping("/success")
    public ApiResponse<String> testSuccess() {
        log.info("测试成功响应");
        return ApiResponse.success("这是一个成功的响应");
    }

    /**
     * 测试带数据的成功响应
     * @return 带数据的成功响应
     */
    @GetMapping("/data")
    public ApiResponse<Map<String, Object>> testData() {
        log.info("测试带数据的成功响应");
        Map<String, Object> data = new HashMap<>();
        data.put("name", "测试用户");
        data.put("age", 25);
        data.put("email", "test@example.com");
        return ApiResponse.success("获取数据成功", data);
    }

    /**
     * 测试创建资源响应
     * @return 创建资源响应
     */
    @PostMapping("/create")
    public ApiResponse<User> testCreate() {
        log.info("测试创建资源响应");
        User user = new User();
        user.setId(100L);
        user.setUsername("newUser");
        user.setPassword("password123");
        user.setEmail("new@example.com");
        return ApiResponse.created(user);
    }

    /**
     * 测试列表数据响应
     * @return 列表数据响应
     */
    @GetMapping("/list")
    public ApiResponse<List<String>> testList() {
        log.info("测试列表数据响应");
        List<String> items = Arrays.asList("项目1", "项目2", "项目3");
        return ApiResponse.success(items);
    }

    /**
     * 测试业务异常
     * @return 不会返回，会抛出异常
     */
    @GetMapping("/business-error")
    public ApiResponse<Void> testBusinessError() {
        log.info("测试业务异常");
        throw new BusinessException(400, "业务处理失败：参数不符合业务规则");
    }

    /**
     * 测试资源未找到异常
     * @param id 资源ID
     * @return 不会返回，会抛出异常
     */
    @GetMapping("/not-found/{id}")
    public ApiResponse<Void> testResourceNotFound(@PathVariable Long id) {
        log.info("测试资源未找到异常，ID: {}", id);
        throw new ResourceNotFoundException("ID为" + id + "的资源");
    }

    /**
     * 测试服务器错误
     * @return 不会返回，会抛出异常
     */
    @GetMapping("/server-error")
    public ApiResponse<Void> testServerError() {
        log.info("测试服务器错误");
        throw new RuntimeException("模拟服务器内部错误");
    }

    /**
     * 测试DNSPod删除记录接口（完整版）
     * @return 删除记录响应
     */
    @PostMapping("/dnspod/delete-record")
    public ApiResponse<Object> testDnspodDeleteRecord() {
        log.info("测试DNSPod删除记录接口（完整版）");
        try {
            DeleteRecordRequest request = new DeleteRecordRequest();
            request.setDomain("cblog.eu");
            request.setRecordId(2167176579L);
            request.setDomainId(1923L);
            
            Object result = dnspodService.deleteRecord(request.getDomain(), request.getRecordId(), request.getDomainId());
            return ApiResponse.success("删除域名解析记录成功", result);
        } catch (Exception e) {
            log.error("删除记录测试失败", e);
            return ApiResponse.error(500, "删除记录测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试DNSPod删除记录接口（简化版）
     * @return 删除记录响应
     */
    @PostMapping("/dnspod/delete-record-simple")
    public ApiResponse<Object> testDnspodDeleteRecordSimple() {
        log.info("测试DNSPod删除记录接口（简化版）");
        try {
            String domain = "cblog.eu";
            Long recordId = 2167176579L;
            
            DeleteRecordRequest request = new DeleteRecordRequest();
            request.setDomain(domain);
            request.setRecordId(recordId);
            
            Object result = dnspodService.deleteRecord(request.getDomain(), request.getRecordId(), request.getDomainId());
            return ApiResponse.success("删除域名解析记录成功", result);
        } catch (Exception e) {
            log.error("删除记录测试失败", e);
            return ApiResponse.error(500, "删除记录测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试DNSPod删除记录接口（错误参数）
     * @return 删除记录响应
     */
    @PostMapping("/dnspod/delete-record-error")
    public ApiResponse<Object> testDnspodDeleteRecordError() {
        log.info("测试DNSPod删除记录接口（错误参数）");
        try {
            DeleteRecordRequest request = new DeleteRecordRequest();
            request.setDomain("invalid-domain.com");
            request.setRecordId(999999L);
            
            Object result = dnspodService.deleteRecord(request.getDomain(), request.getRecordId(), request.getDomainId());
            return ApiResponse.success("删除域名解析记录成功", result);
        } catch (Exception e) {
            log.error("删除记录测试失败（预期错误）", e);
            return ApiResponse.error(500, "删除记录测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试DNSPod修改记录接口
     * @return 修改记录响应
     */
    @PostMapping("/dnspod/modify-record")
    public ApiResponse<Object> testDnspodModifyRecord() {
        log.info("测试DNSPod修改记录接口");
        try {
            ModifyRecordRequest request = new ModifyRecordRequest();
            request.setDomain("cblog.eu");
            request.setRecordType("A");
            request.setRecordLine("默认");
            request.setValue("192.168.1.100");
            request.setRecordId(2167176579L);
            request.setSubDomain("test");
            request.setTtl(600);
            request.setRemark("测试修改记录");
            
            Object result = dnspodService.modifyRecord(request);
            return ApiResponse.success("修改域名解析记录成功", result);
        } catch (Exception e) {
            log.error("修改记录测试失败", e);
            return ApiResponse.error(500, "修改记录测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试DNSPod修改记录接口（简化版）
     * @return 修改记录响应
     */
    @PostMapping("/dnspod/modify-record-simple")
    public ApiResponse<Object> testDnspodModifyRecordSimple() {
        log.info("测试DNSPod修改记录接口（简化版）");
        try {
            ModifyRecordRequest request = new ModifyRecordRequest();
            request.setDomain("cblog.eu");
            request.setRecordType("A");
            request.setRecordLine("默认");
            request.setValue("192.168.1.200");
            request.setRecordId(2167176579L);
            
            Object result = dnspodService.modifyRecord(request);
            return ApiResponse.success("修改域名解析记录成功", result);
        } catch (Exception e) {
            log.error("修改记录测试失败", e);
            return ApiResponse.error(500, "修改记录测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试DNSPod修改记录接口（错误参数）
     * @return 修改记录响应
     */
    @PostMapping("/dnspod/modify-record-error")
    public ApiResponse<Object> testDnspodModifyRecordError() {
        log.info("测试DNSPod修改记录接口（错误参数）");
        try {
            ModifyRecordRequest request = new ModifyRecordRequest();
            request.setDomain("invalid-domain.com");
            request.setRecordType("A");
            request.setRecordLine("默认");
            request.setValue("192.168.1.1");
            request.setRecordId(999999L);
            
            Object result = dnspodService.modifyRecord(request);
            return ApiResponse.success("修改域名解析记录成功", result);
        } catch (Exception e) {
            log.error("修改记录测试失败（预期错误）", e);
            return ApiResponse.error(500, "修改记录测试失败: " + e.getMessage());
        }
    }
}
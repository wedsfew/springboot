package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.BusinessException;
import com.example.demo.common.ResourceNotFoundException;
import com.example.demo.entity.User;
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
        throw new RuntimeException("服务器内部错误测试");
    }
}
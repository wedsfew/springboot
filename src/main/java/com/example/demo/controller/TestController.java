package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 
 * @RestController - 组合了@Controller和@ResponseBody，返回JSON数据
 * @Slf4j - 自动生成日志对象
 */
@RestController
@Slf4j
public class TestController {
    
    /**
     * 简单的健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        log.info("健康检查请求");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "应用正常运行中");
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}

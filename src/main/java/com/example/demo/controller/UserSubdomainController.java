package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.UserSubdomain;
import com.example.demo.service.UserSubdomainService;
import com.example.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户3级域名控制器
 * 
 * @author CodeBuddy
 * @since 2025-08-24
 */
@RestController
@RequestMapping("/api/user/subdomains")
@Slf4j
public class UserSubdomainController {
    
    @Autowired
    private UserSubdomainService userSubdomainService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 创建用户子域名
     * 
     * @param userId 用户ID
     * @param subdomain 子域名前缀
     * @param domain 主域名
     * @param remark 备注信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public ApiResponse<UserSubdomain> createSubdomain(
            @RequestParam Long userId,
            @RequestParam String subdomain,
            @RequestParam String domain,
            @RequestParam(required = false) String remark) {
        try {
            UserSubdomain result = userSubdomainService.createSubdomain(userId, subdomain, domain, remark);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "创建子域名失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户子域名
     * 
     * @param id 主键ID
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public ApiResponse<UserSubdomain> getById(@PathVariable Long id) {
        UserSubdomain result = userSubdomainService.getById(id);
        if (result != null) {
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(404, "未找到指定的子域名");
        }
    }
    
    /**
     * 根据用户ID查询子域名列表
     * 
     * @param userId 用户ID
     * @return 查询结果
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<UserSubdomain>> getByUserId(@PathVariable Long userId) {
        try {
            List<UserSubdomain> result = userSubdomainService.getByUserId(userId);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    /**
     * 根据完整域名查询
     * 
     * @param fullDomain 完整域名
     * @return 查询结果
     */
    @GetMapping("/domain/{fullDomain}")
    public ApiResponse<UserSubdomain> getByFullDomain(@PathVariable String fullDomain) {
        UserSubdomain result = userSubdomainService.getByFullDomain(fullDomain);
        if (result != null) {
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(404, "未找到指定的域名");
        }
    }
    
    /**
     * 检查域名是否可用
     * 
     * @param fullDomain 完整域名
     * @return 检查结果
     */
    @GetMapping("/check/{fullDomain}")
    public ApiResponse<Boolean> checkDomainAvailable(@PathVariable String fullDomain) {
        boolean available = userSubdomainService.isDomainAvailable(fullDomain);
        return ApiResponse.success(available);
    }
    
    /**
     * 更新用户子域名状态
     * 
     * @param id 主键ID
     * @param status 新状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean result = userSubdomainService.updateStatus(id, status);
            if (result) {
                return ApiResponse.success(true);
            } else {
                return ApiResponse.error(400, "更新状态失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    /**
     * 更新用户子域名备注
     * 
     * @param id 主键ID
     * @param remark 备注信息
     * @return 更新结果
     */
    @PutMapping("/{id}/remark")
    public ApiResponse<Boolean> updateRemark(@PathVariable Long id, @RequestParam String remark) {
        boolean result = userSubdomainService.updateRemark(id, remark);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(400, "更新备注失败");
        }
    }
    
    /**
     * 删除用户子域名（软删除）
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteById(@PathVariable Long id) {
        boolean result = userSubdomainService.deleteById(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(400, "删除子域名失败");
        }
    }
    
    /**
     * 根据用户ID和状态查询子域名列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 查询结果
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<List<UserSubdomain>> getByUserIdAndStatus(
            @PathVariable Long userId, 
            @PathVariable String status) {
        try {
            List<UserSubdomain> result = userSubdomainService.getByUserIdAndStatus(userId, status);
            return ApiResponse.success(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    /**
     * 查询所有活跃的子域名
     * 
     * @return 查询结果
     */
    @GetMapping("/active")
    public ApiResponse<List<UserSubdomain>> getAllActive() {
        List<UserSubdomain> result = userSubdomainService.getAllActive();
        return ApiResponse.success(result);
    }
    
    /**
     * 获取当前登录用户的3级域名列表
     * 
     * @param authHeader Authorization头信息
     * @param status 可选的状态过滤参数
     * @return 当前用户的域名列表
     */
    @GetMapping("/list/mine")
    public ApiResponse<List<UserSubdomain>> getMyDomains(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String status) {
        try {
            // 从Authorization头中提取token
            String token = authHeader.replace("Bearer ", "");
            
            // 从token中获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            List<UserSubdomain> result;
            if (status != null && !status.isEmpty()) {
                // 如果提供了状态参数，则按状态过滤
                result = userSubdomainService.getByUserIdAndStatus(userId, status);
            } else {
                // 否则获取所有非删除状态的域名
                result = userSubdomainService.getByUserId(userId);
            }
            
            log.info("用户 {} 查询了自己的域名列表，共 {} 条记录", userId, result.size());
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            log.error("获取用户域名列表失败", e);
            return ApiResponse.error(500, "获取域名列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询域名是否已注册
     * 
     * @param request 包含subdomain和domain的JSON请求体
     * @return 查询结果
     */
    @PostMapping("/check-registration")
    public ApiResponse<String> checkDomainRegistration(@RequestBody CheckDomainRequest request) {
        try {
            UserSubdomain result = userSubdomainService.checkDomainRegistration(request.getSubdomain(), request.getDomain());
            String fullDomain = request.getSubdomain() + "." + request.getDomain();
            
            if (result != null) {
                return ApiResponse.success("域名 " + fullDomain + " 已注册");
            } else {
                return ApiResponse.success("域名 " + fullDomain + " 未注册");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "查询域名注册状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 用户注册3级域名
     * 
     * @param request 包含subdomain、domain的JSON请求体
     * @param authHeader Authorization头信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ApiResponse<UserSubdomain> registerSubdomain(
            @RequestBody RegisterSubdomainRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // 从Authorization头中提取token
            String token = authHeader.replace("Bearer ", "");
            
            // 从token中获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String email = jwtUtil.getEmailFromToken(token);
            
            // 先检查域名是否已注册
            UserSubdomain existingDomain = userSubdomainService.checkDomainRegistration(
                request.getSubdomain(), request.getDomain());
            
            if (existingDomain != null) {
                String fullDomain = request.getSubdomain() + "." + request.getDomain();
                return ApiResponse.error(400, "域名 " + fullDomain + " 已注册");
            }
            
            // 创建新的域名记录
            UserSubdomain result = userSubdomainService.createSubdomain(
                userId, request.getSubdomain(), request.getDomain(), email);
            
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "注册域名失败：" + e.getMessage());
        }
    }

    /**
     * 域名查询请求DTO
     */
    public static class CheckDomainRequest {
        private String subdomain;
        private String domain;
        
        public String getSubdomain() {
            return subdomain;
        }
        
        public void setSubdomain(String subdomain) {
            this.subdomain = subdomain;
        }
        
        public String getDomain() {
            return domain;
        }
        
        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
    
    /**
     * 域名注册请求DTO
     */
    public static class RegisterSubdomainRequest {
        private String subdomain;
        private String domain;
        
        public String getSubdomain() {
            return subdomain;
        }
        
        public void setSubdomain(String subdomain) {
            this.subdomain = subdomain;
        }
        
        public String getDomain() {
            return domain;
        }
        
        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
}
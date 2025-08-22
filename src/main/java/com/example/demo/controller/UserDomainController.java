package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.SubdomainRegisterRequest;
import com.example.demo.entity.UserSubdomain;
import com.example.demo.service.DnspodService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserSubdomainService;
import com.example.demo.util.JwtUtil;
import com.tencentcloudapi.dnspod.v20210323.models.CreateRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件名：UserDomainController.java
 * 功能：用户域名管理控制器，提供用户注册三级域名等功能
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 更新时间：2025-08-22
 * 版本：v1.1.0
 * 更新说明：添加了将用户注册的三级域名信息保存到数据库的功能
 */
@RestController
@RequestMapping("/api/user/domains")
public class UserDomainController {

    @Autowired
    private DnspodService dnspodService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSubdomainService userSubdomainService;
    
    /**
     * 用户注册三级域名
     * 
     * 业务流程：
     * 1. 从JWT token中获取用户邮箱
     * 2. 检查三级域名是否可用
     * 3. 如果可用，添加A记录，并将用户邮箱作为备注
     * 4. 返回注册结果
     * 
     * @param request 注册三级域名请求
     * @param httpRequest HTTP请求对象，用于获取Authorization头
     * @return 注册结果
     */
    @PostMapping("/register")
    public ApiResponse<?> registerSubdomain(@RequestBody SubdomainRegisterRequest request, 
                                           HttpServletRequest httpRequest) {
        try {
            // 1. 从JWT token中获取用户邮箱
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ApiResponse.error(401, "未授权：需要有效的JWT令牌");
            }
            
            String token = authHeader.substring(7);
            String userEmail = jwtUtil.getEmailFromToken(token);
            
            if (userEmail == null || userEmail.isEmpty()) {
                return ApiResponse.error(401, "无法获取用户邮箱信息");
            }
            
            // 2. 验证请求参数
            if (request.getSubDomain() == null || request.getSubDomain().isEmpty()) {
                return ApiResponse.error(400, "子域名前缀不能为空");
            }
            
            if (request.getValue() == null || request.getValue().isEmpty()) {
                return ApiResponse.error(400, "IP地址不能为空");
            }
            
            // 设置默认值
            String domain = request.getDomain() != null ? request.getDomain() : "cblog.eu";
            Long ttl = request.getTtl() != null ? request.getTtl() : 600L;
            
            // 3. 检查三级域名是否可用
            DescribeRecordFilterListResponse checkResponse = dnspodService.getRecordFilterList(
                domain, null, request.getSubDomain(), null, 10, 0);
            
            boolean isAvailable = checkResponse.getRecordCountInfo().getTotalCount() == 0;
            
            if (!isAvailable) {
                return ApiResponse.error(409, "域名 " + request.getSubDomain() + "." + domain + " 已被注册");
            }
            
            // 4. 添加A记录，并将用户邮箱作为备注
            CreateRecordResponse response = dnspodService.createRecord(
                domain,                 // 域名
                "A",                    // 记录类型
                "默认",                  // 记录线路
                request.getValue(),     // 记录值（IP地址）
                request.getSubDomain(), // 子域名前缀
                ttl,                    // TTL值
                null,                   // MX优先级
                null,                   // 权重
                "ENABLE",               // 状态
                userEmail               // 备注（用户邮箱）
            );
            
            // 5. 保存用户三级域名记录到数据库
            Long userId = jwtUtil.getUserIdFromToken(token); // 从token中获取用户ID
            
            UserSubdomain userSubdomain = new UserSubdomain();
            userSubdomain.setUserId(userId);
            userSubdomain.setSubdomain(request.getSubDomain());
            userSubdomain.setDomain(domain);
            userSubdomain.setRecordId(Long.valueOf(response.getRecordId()));
            userSubdomain.setIpAddress(request.getValue());
            userSubdomain.setTtl(ttl.intValue());
            userSubdomain.setStatus("ACTIVE");
            
            userSubdomainService.saveUserSubdomain(userSubdomain);
            
            // 6. 返回注册结果
            return ApiResponse.success("域名 " + request.getSubDomain() + "." + domain + " 注册成功", response);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "注册三级域名失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询用户的三级域名列表
     * 
     * @param httpRequest HTTP请求对象，用于获取Authorization头
     * @return 用户的三级域名列表
     */
    @GetMapping("/subdomains")
    public ApiResponse<?> getUserSubdomains(HttpServletRequest httpRequest) {
        try {
            // 1. 从JWT token中获取用户ID
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ApiResponse.error(401, "未授权：需要有效的JWT令牌");
            }
            
            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            // 2. 查询用户的三级域名列表
            java.util.List<UserSubdomain> userSubdomains = userSubdomainService.findByUserId(userId);
            
            // 3. 返回结果
            return ApiResponse.success("查询成功", userSubdomains);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "查询用户三级域名列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户的三级域名（逻辑删除）
     * 
     * @param id 三级域名记录ID
     * @param httpRequest HTTP请求对象，用于获取Authorization头
     * @return 删除结果
     */
    @DeleteMapping("/subdomains/{id}")
    public ApiResponse<?> deleteUserSubdomain(@PathVariable Long id, HttpServletRequest httpRequest) {
        try {
            // 1. 从JWT token中获取用户ID
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ApiResponse.error(401, "未授权：需要有效的JWT令牌");
            }
            
            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            // 2. 查询三级域名记录
            UserSubdomain userSubdomain = userSubdomainService.findById(id);
            
            // 3. 验证记录是否存在
            if (userSubdomain == null) {
                return ApiResponse.error(404, "三级域名记录不存在");
            }
            
            // 4. 验证记录是否属于当前用户
            if (!userSubdomain.getUserId().equals(userId)) {
                return ApiResponse.error(403, "无权操作：该三级域名不属于当前用户");
            }
            
            // 5. 删除DNSPod记录
            dnspodService.deleteRecord(userSubdomain.getDomain(), userSubdomain.getRecordId(), null);
            
            // 6. 逻辑删除数据库记录
            userSubdomainService.deleteUserSubdomain(id);
            
            // 7. 返回结果
            return ApiResponse.success("三级域名删除成功", null);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "删除三级域名失败: " + e.getMessage());
        }
    }
}

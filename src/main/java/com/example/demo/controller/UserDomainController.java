package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.FourLevelDomainRegisterRequest;
import com.example.demo.dto.SubdomainRegisterRequest;
import com.example.demo.entity.UserSubdomain;
import com.example.demo.mapper.DomainMapper;
import com.example.demo.service.DnspodService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserSubdomainService;
import com.example.demo.util.JwtUtil;
import com.tencentcloudapi.dnspod.v20210323.models.CreateRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件名：UserDomainController.java
 * 功能：用户域名管理控制器，提供用户注册三级域名等功能
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 更新时间：2025-08-23
 * 版本：v1.3.0
 * 更新说明：新增用户添加4级域名记录接口
 */
@RestController
@RequestMapping("/api/user/domains")
@Slf4j
public class UserDomainController {

    @Autowired
    private DnspodService dnspodService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @SuppressWarnings("unused")
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSubdomainService userSubdomainService;
    
    @Autowired
    private DomainMapper domainMapper;
    
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
            
            // 2.1 验证域名后缀是否有效
            List<String> availableDomains = domainMapper.findAvailableDomainSuffixes();
            if (!availableDomains.contains(domain)) {
                return ApiResponse.error(400, "无效的域名后缀：" + domain + "，请使用/api/domains/suffixes接口获取可用的域名后缀");
            }
            
            // 3. 检查三级域名是否可用
            boolean isAvailable = false;
            try {
                DescribeRecordListResponse checkResponse = dnspodService.getRecordList(
                    domain, null, request.getSubDomain(), null, null, null,
                    null, null, null, null, 0, 10);
                
                isAvailable = checkResponse.getRecordList() == null || checkResponse.getRecordList().length == 0;
            } catch (Exception e) {
                // 如果异常消息包含"记录列表为空"，则认为域名可用
                if (e.getMessage() != null && e.getMessage().contains("记录列表为空")) {
                    isAvailable = true;
                } else {
                    // 记录错误但继续执行，不要抛出异常
                    System.err.println("检查域名可用性时发生错误: " + e.getMessage());
                    // 默认域名可用
                    isAvailable = true;
                }
            }
            
            if (!isAvailable) {
                return ApiResponse.error(409, "域名 " + request.getSubDomain() + "." + domain + " 已被注册");
            }
            
            // 4. 添加A记录，并将用户邮箱作为备注
            CreateRecordResponse recordResponse = null;
            try {
                recordResponse = dnspodService.createRecord(
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
            } catch (Exception e) {
                return ApiResponse.error(500, "注册三级域名失败: " + e.getMessage());
            }
            
            // 检查是否成功创建记录
            if (recordResponse == null) {
                return ApiResponse.error(500, "注册三级域名失败: 无法创建DNS记录");
            }
            
            // 5. 保存用户三级域名记录到数据库
            Long userId = jwtUtil.getUserIdFromToken(token); // 从token中获取用户ID
            
            UserSubdomain userSubdomain = new UserSubdomain();
            userSubdomain.setUserId(userId);
            userSubdomain.setSubdomain(request.getSubDomain());
            userSubdomain.setDomain(domain);
            userSubdomain.setRecordId(Long.valueOf(recordResponse.getRecordId()));
            userSubdomain.setIpAddress(request.getValue());
            userSubdomain.setTtl(ttl.intValue());
            userSubdomain.setStatus("ACTIVE");
            
            userSubdomainService.saveUserSubdomain(userSubdomain);
            
            // 6. 返回注册结果
            return ApiResponse.success("域名 " + request.getSubDomain() + "." + domain + " 注册成功", recordResponse);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "注册三级域名失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户添加4级域名记录
     * 
     * 业务流程：
     * 1. 从JWT token中获取用户邮箱
     * 2. 验证请求参数
     * 3. 拼接subDomain（前端的threeDomain和subDomain拼接而成）
     * 4. 添加A记录，并将用户邮箱作为备注
     * 5. 返回注册结果
     * 
     * @param request 注册4级域名请求
     * @param httpRequest HTTP请求对象，用于获取Authorization头
     * @return 注册结果
     */
    @PostMapping("/register/four-level")
    public ApiResponse<?> registerFourLevelDomain(@RequestBody FourLevelDomainRegisterRequest request, 
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
                return ApiResponse.error(400, "三级域名前缀不能为空");
            }
            
            if (request.getThreeDomain() == null || request.getThreeDomain().isEmpty()) {
                return ApiResponse.error(400, "四级域名前缀不能为空");
            }
            
            if (request.getValue() == null || request.getValue().isEmpty()) {
                return ApiResponse.error(400, "IP地址不能为空");
            }
            
            // 设置默认值
            String domain = request.getDomain() != null ? request.getDomain() : "cblog.eu";
            String recordType = request.getRecordType() != null ? request.getRecordType() : "A";
            String recordLine = request.getRecordLine() != null ? request.getRecordLine() : "默认";
            Long ttl = request.getTtl() != null ? request.getTtl() : 600L;
            
            // 2.1 验证域名后缀是否有效
            List<String> availableDomains = domainMapper.findAvailableDomainSuffixes();
            if (!availableDomains.contains(domain)) {
                return ApiResponse.error(400, "无效的域名后缀：" + domain + "，请使用/api/domains/suffixes接口获取可用的域名后缀");
            }
            
            // 3. 拼接subDomain（前端的threeDomain和subDomain拼接而成）
            String combinedSubDomain = request.getThreeDomain() + "." + request.getSubDomain();
            
            // 4. 添加A记录，并将用户邮箱作为备注
            // 备注格式：subDomain + email
            String remark = request.getSubDomain() + "." + userEmail;
            
            CreateRecordResponse recordResponse = null;
            try {
                recordResponse = dnspodService.createRecord(
                    domain,                 // 域名
                    recordType,             // 记录类型
                    recordLine,             // 记录线路
                    request.getValue(),     // 记录值（IP地址）
                    combinedSubDomain,      // 拼接后的子域名前缀
                    ttl,                    // TTL值
                    null,                   // MX优先级
                    null,                   // 权重
                    "ENABLE",               // 状态
                    remark                  // 备注（subDomain + email）
                );
            } catch (Exception e) {
                return ApiResponse.error(500, "添加4级域名记录失败: " + e.getMessage());
            }
            
            // 检查是否成功创建记录
            if (recordResponse == null) {
                return ApiResponse.error(500, "添加4级域名记录失败: 无法创建DNS记录");
            }
            
            // 5. 保存用户域名记录到数据库
            Long userId = jwtUtil.getUserIdFromToken(token); // 从token中获取用户ID
            
            UserSubdomain userSubdomain = new UserSubdomain();
            userSubdomain.setUserId(userId);
            userSubdomain.setSubdomain(combinedSubDomain);  // 保存完整的四级域名前缀
            userSubdomain.setDomain(domain);
            userSubdomain.setRecordId(Long.valueOf(recordResponse.getRecordId()));
            userSubdomain.setIpAddress(request.getValue());
            userSubdomain.setTtl(ttl.intValue());
            userSubdomain.setStatus("ACTIVE");
            
            userSubdomainService.saveUserSubdomain(userSubdomain);
            
            // 6. 返回注册结果
            return ApiResponse.success("域名 " + combinedSubDomain + "." + domain + " 添加成功", recordResponse);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "添加4级域名记录失败: " + e.getMessage());
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
     * 删除用户的三级域名（物理删除）
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
            boolean dnspodDeleteSuccess = true;
            String dnspodErrorMessage = null;
            try {
                dnspodService.deleteRecord(userSubdomain.getDomain(), userSubdomain.getRecordId(), null);
            } catch (Exception e) {
                // 如果DNSPod记录删除失败，但错误信息包含"记录编号错误"，说明记录可能已经被删除
                // 这种情况下我们仍然继续删除数据库中的记录
                if (e.getMessage() == null || !e.getMessage().contains("记录编号错误")) {
                    // 记录错误但不抛出异常，确保数据库记录仍然被删除
                    dnspodDeleteSuccess = false;
                    dnspodErrorMessage = e.getMessage();
                    log.error("DNSPod记录删除失败: {}", e.getMessage(), e);
                }
                System.out.println("警告：DNSPod记录可能已被删除或删除失败，继续删除数据库记录: " + e.getMessage());
            }
            
            // 6. 物理删除数据库记录
            boolean dbDeleteSuccess = userSubdomainService.deleteUserSubdomain(id);
            
            // 7. 返回结果，根据DNSPod和数据库操作的结果给出适当的消息
            if (!dnspodDeleteSuccess) {
                // DNSPod删除失败但数据库删除成功
                if (dbDeleteSuccess) {
                    return ApiResponse.success("三级域名在本地数据库删除成功，但DNSPod删除失败: " + dnspodErrorMessage, null);
                } else {
                    // 两者都失败
                    return ApiResponse.error(500, "三级域名删除失败: DNSPod和数据库操作均失败");
                }
            }
            
            // 两者都成功
            return ApiResponse.success("三级域名删除成功", null);
            
        } catch (Exception e) {
            return ApiResponse.error(500, "删除三级域名失败: " + e.getMessage());
        }
    }
}
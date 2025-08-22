package com.example.demo.service;

import com.example.demo.entity.UserSubdomain;

import java.util.List;

/**
 * 文件名：UserSubdomainService.java
 * 功能：用户三级域名服务接口
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
public interface UserSubdomainService {
    
    /**
     * 保存用户三级域名记录
     * 
     * @param userSubdomain 用户三级域名实体
     * @return 保存后的用户三级域名实体
     */
    UserSubdomain saveUserSubdomain(UserSubdomain userSubdomain);
    
    /**
     * 根据ID查询用户三级域名记录
     * 
     * @param id 记录ID
     * @return 用户三级域名实体
     */
    UserSubdomain findById(Long id);
    
    /**
     * 根据用户ID查询用户三级域名记录列表
     * 
     * @param userId 用户ID
     * @return 用户三级域名实体列表
     */
    List<UserSubdomain> findByUserId(Long userId);
    
    /**
     * 根据子域名和主域名查询用户三级域名记录
     * 
     * @param subdomain 子域名前缀
     * @param domain 主域名
     * @return 用户三级域名实体
     */
    UserSubdomain findBySubdomainAndDomain(String subdomain, String domain);
    
    /**
     * 更新用户三级域名记录状态
     * 
     * @param id 记录ID
     * @param status 状态
     * @return 更新后的用户三级域名实体
     */
    UserSubdomain updateStatus(Long id, String status);
    
    /**
     * 更新用户三级域名记录IP地址
     * 
     * @param id 记录ID
     * @param ipAddress IP地址
     * @return 更新后的用户三级域名实体
     */
    UserSubdomain updateIpAddress(Long id, String ipAddress);
    
    /**
     * 删除用户三级域名记录（逻辑删除）
     * 
     * @param id 记录ID
     * @return 是否删除成功
     */
    boolean deleteUserSubdomain(Long id);
    
    /**
     * 查询用户三级域名记录总数
     * 
     * @param userId 用户ID
     * @return 记录总数
     */
    int countByUserId(Long userId);
    
    /**
     * 查询所有活跃的用户三级域名记录
     * 
     * @return 用户三级域名实体列表
     */
    List<UserSubdomain> findAllActive();
    
    /**
     * 根据状态查询用户三级域名记录
     * 
     * @param status 状态
     * @return 用户三级域名实体列表
     */
    List<UserSubdomain> findByStatus(String status);
}
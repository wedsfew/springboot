package com.example.demo.service;

import com.example.demo.entity.Admin;

/**
 * 文件名：AdminService.java
 * 功能：管理员服务接口，定义管理员相关业务逻辑
 * 作者：CodeBuddy
 * 创建时间：2025-08-11
 * 版本：v1.0.0
 */
public interface AdminService {
    
    /**
     * 管理员注册
     * @param admin 管理员信息
     * @return 注册成功的管理员信息（不含密码）
     * @throws RuntimeException 如果用户名或邮箱已存在
     */
    Admin register(Admin admin);
    
    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的管理员信息（不含密码）
     * @throws RuntimeException 如果用户名不存在或密码错误
     */
    Admin login(String username, String password);
    
    /**
     * 根据ID查询管理员
     * @param id 管理员ID
     * @return 管理员信息（不含密码）
     */
    Admin findById(Long id);
    
    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员信息（不含密码）
     */
    Admin findByUsername(String username);
    
    /**
     * 更新管理员信息
     * @param admin 管理员信息
     * @return 更新后的管理员信息（不含密码）
     */
    Admin update(Admin admin);
}
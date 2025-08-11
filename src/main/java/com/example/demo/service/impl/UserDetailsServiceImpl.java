package com.example.demo.service.impl;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 文件名：UserDetailsServiceImpl.java
 * 功能：实现Spring Security的UserDetailsService接口，用于加载用户信息
 * 作者：CodeBuddy
 * 创建时间：2025-08-11
 * 版本：v1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;

    /**
     * 根据用户名、邮箱或ID加载用户信息
     * 支持通过用户名、邮箱或ID查找用户
     * @param principal 用户名、邮箱或ID
     * @return UserDetails对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        // 尝试将principal解析为用户ID
        try {
            Long userId = Long.parseLong(principal);
            
            // 先尝试查找普通用户
            User user = userMapper.findById(userId);
            if (user != null) {
                // 创建UserDetails对象，设置用户角色
                String role = user.getRole() != null ? user.getRole() : "ROLE_USER";
                return new org.springframework.security.core.userdetails.User(
                        user.getId().toString(), // 使用用户ID作为Spring Security的用户标识
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                );
            }
            
            // 如果不是普通用户，尝试查找管理员
            Admin admin = adminMapper.findById(userId);
            if (admin != null) {
                // 创建UserDetails对象，设置管理员角色
                String role = admin.getRole() != null ? admin.getRole() : "ROLE_ADMIN";
                return new org.springframework.security.core.userdetails.User(
                        admin.getId().toString(), // 使用管理员ID作为Spring Security的用户标识
                        admin.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                );
            }
            
            throw new UsernameNotFoundException("用户不存在: " + principal);
        } catch (NumberFormatException e) {
            // 如果不是数字，则尝试通过用户名或邮箱查找
            
            // 先尝试查找普通用户
            User user = userMapper.findByEmail(principal);
            if (user == null) {
                user = userMapper.findByUsername(principal);
            }
            
            if (user != null) {
                // 创建UserDetails对象，设置用户角色
                String role = user.getRole() != null ? user.getRole() : "ROLE_USER";
                return new org.springframework.security.core.userdetails.User(
                        user.getId().toString(), // 使用用户ID作为Spring Security的用户标识
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                );
            }
            
            // 如果不是普通用户，尝试查找管理员
            Admin admin = adminMapper.findByEmail(principal);
            if (admin == null) {
                admin = adminMapper.findByUsername(principal);
            }
            
            if (admin != null) {
                // 创建UserDetails对象，设置管理员角色
                String role = admin.getRole() != null ? admin.getRole() : "ROLE_ADMIN";
                return new org.springframework.security.core.userdetails.User(
                        admin.getId().toString(), // 使用管理员ID作为Spring Security的用户标识
                        admin.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                );
            }
            
            throw new UsernameNotFoundException("用户不存在: " + principal);
        }
    }
}
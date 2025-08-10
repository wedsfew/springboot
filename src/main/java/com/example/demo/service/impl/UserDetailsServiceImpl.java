package com.example.demo.service.impl;

import com.example.demo.entity.User;
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

    /**
     * 根据用户名、邮箱或ID加载用户信息
     * 支持通过用户名、邮箱或ID查找用户
     * @param principal 用户名、邮箱或ID
     * @return UserDetails对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        User user = null;
        
        // 尝试将principal解析为用户ID
        try {
            Long userId = Long.parseLong(principal);
            user = userMapper.findById(userId);
        } catch (NumberFormatException e) {
            // 如果不是数字，则尝试通过邮箱查找
            user = userMapper.findByEmail(principal);
            
            // 如果通过邮箱没找到，再尝试通过用户名查找
            if (user == null) {
                user = userMapper.findByUsername(principal);
            }
        }
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + principal);
        }
        
        // 创建UserDetails对象，暂时只设置基本角色ROLE_USER
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(), // 使用用户ID作为Spring Security的用户标识
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
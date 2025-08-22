package com.example.demo.service.impl;

import com.example.demo.entity.UserSubdomain;
import com.example.demo.mapper.UserSubdomainMapper;
import com.example.demo.service.UserSubdomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文件名：UserSubdomainServiceImpl.java
 * 功能：用户三级域名服务实现类
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSubdomainServiceImpl implements UserSubdomainService {
    
    private final UserSubdomainMapper userSubdomainMapper;
    
    @Override
    @Transactional
    public UserSubdomain saveUserSubdomain(UserSubdomain userSubdomain) {
        log.info("保存用户三级域名记录: {}.{}", userSubdomain.getSubdomain(), userSubdomain.getDomain());
        userSubdomainMapper.insert(userSubdomain);
        return userSubdomain;
    }
    
    @Override
    public UserSubdomain findById(Long id) {
        log.debug("根据ID查询用户三级域名记录: {}", id);
        return userSubdomainMapper.findById(id);
    }
    
    @Override
    public List<UserSubdomain> findByUserId(Long userId) {
        log.debug("根据用户ID查询用户三级域名记录列表: {}", userId);
        return userSubdomainMapper.findByUserId(userId);
    }
    
    @Override
    public UserSubdomain findBySubdomainAndDomain(String subdomain, String domain) {
        log.debug("根据子域名和主域名查询用户三级域名记录: {}.{}", subdomain, domain);
        return userSubdomainMapper.findBySubdomainAndDomain(subdomain, domain);
    }
    
    @Override
    @Transactional
    public UserSubdomain updateStatus(Long id, String status) {
        log.debug("更新用户三级域名记录状态: {} -> {}", id, status);
        userSubdomainMapper.updateStatus(id, status);
        return userSubdomainMapper.findById(id);
    }
    
    @Override
    @Transactional
    public UserSubdomain updateIpAddress(Long id, String ipAddress) {
        log.debug("更新用户三级域名记录IP地址: {} -> {}", id, ipAddress);
        userSubdomainMapper.updateIpAddress(id, ipAddress);
        return userSubdomainMapper.findById(id);
    }
    
    @Override
    @Transactional
    public boolean deleteUserSubdomain(Long id) {
        log.debug("删除用户三级域名记录: {}", id);
        return userSubdomainMapper.delete(id) > 0;
    }
    
    @Override
    public int countByUserId(Long userId) {
        log.debug("查询用户三级域名记录总数: {}", userId);
        return userSubdomainMapper.countByUserId(userId);
    }
    
    @Override
    public List<UserSubdomain> findAllActive() {
        log.debug("查询所有活跃的用户三级域名记录");
        return userSubdomainMapper.findAllActive();
    }
    
    @Override
    public List<UserSubdomain> findByStatus(String status) {
        log.debug("根据状态查询用户三级域名记录: {}", status);
        return userSubdomainMapper.findByStatus(status);
    }
}
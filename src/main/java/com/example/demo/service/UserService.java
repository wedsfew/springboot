package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 查询所有用户
     */
    List<User> findAllUsers();
    
    /**
     * 根据ID查询用户
     */
    User findUserById(Long id);
}
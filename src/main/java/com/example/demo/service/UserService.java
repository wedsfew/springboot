package com.example.demo.service;

import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.dto.UserRegisterVerifyRequest;
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
    
    /**
     * 保存用户
     */
    User saveUser(User user);
    
    /**
     * 更新用户
     */
    User updateUser(User user);
    
    /**
     * 删除用户
     */
    boolean deleteUserById(Long id);
    
    /**
     * 用户注册第一步：发送验证码
     * @param request 包含邮箱和密码的注册请求
     * @return 是否成功发送验证码
     */
    boolean registerStep1(UserRegisterRequest request);
    
    /**
     * 用户注册第二步：验证验证码并完成注册
     * @param request 包含邮箱、密码和验证码的注册验证请求
     * @return 注册成功的用户信息
     */
    User registerStep2(UserRegisterVerifyRequest request);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱地址
     * @return 用户信息，如果不存在则返回null
     */
    User findUserByEmail(String email);
}

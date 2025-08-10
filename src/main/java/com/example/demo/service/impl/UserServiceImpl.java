package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 * 
 * @Service - 标记为服务层组件
 * @RequiredArgsConstructor - 自动生成带有final字段的构造函数，实现依赖注入
 * @Slf4j - 自动生成日志对象
 * @Transactional - 事务管理
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    // 使用final + @RequiredArgsConstructor代替@Autowired，更加安全和简洁
    private final UserMapper userMapper;
    
    @Override
    public List<User> findAllUsers() {
        log.debug("查询所有用户");
        return userMapper.findAll();
    }
    
    @Override
    public User findUserById(Long id) {
        log.debug("根据ID查询用户: {}", id);
        return userMapper.findById(id);
    }
    
    @Override
    @Transactional
    public User saveUser(User user) {
        log.debug("保存用户: {}", user);
        userMapper.save(user);
        return user;
    }
    
    @Override
    @Transactional
    public User updateUser(User user) {
        log.debug("更新用户: {}", user);
        userMapper.update(user);
        return user;
    }
    
    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        log.debug("删除用户: {}", id);
        return userMapper.deleteById(id) > 0;
    }
}

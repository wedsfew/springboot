package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.ResourceNotFoundException;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 
 * @RestController - 组合了@Controller和@ResponseBody，返回JSON数据
 * @RequestMapping - 定义请求路径
 * @RequiredArgsConstructor - 自动生成带有final字段的构造函数，实现依赖注入
 * @Slf4j - 自动生成日志对象
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        log.info("获取所有用户");
        List<User> users = userService.findAllUsers();
        
        return ApiResponse.success(users);
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        log.info("获取用户接口获取用户接口获取用户接口获取用户接口获取用户接口获取用户接口");
        log.info("获取用户ID: {}", id);
        User user = userService.findUserById(id);
        if (user != null) {
            return ApiResponse.success(user);
        } else {
            throw new ResourceNotFoundException("用户");
        }
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponse<User> createUser(@RequestBody User user) {
        log.info("创建用户: {}", user);
        System.out.println(user);
        User savedUser = userService.saveUser(user);
        return ApiResponse.created(savedUser);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("更新用户接口更新用户接口更新用户接口更新用户接口更新用户接口");
        log.info("更新用户ID: {}, 用户信息: {}", id, user);
        System.out.println(user);
        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("用户");
        }
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ApiResponse.success("用户更新成功", updatedUser);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户删除用户删除用户删除用户删除用户删除用户");
        log.info("删除用户ID: {}", id);
        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("用户");
        }
        userService.deleteUserById(id);
        return ApiResponse.success("用户删除成功", null);
    }
}

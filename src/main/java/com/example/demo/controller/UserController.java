package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("获取所有用户");
        return ResponseEntity.ok(userService.findAllUsers());
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("获取用户ID: {}", id);
        User user = userService.findUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("创建用户: {}", user);
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("更新用户ID: {}, 用户信息: {}", id, user);
        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        log.info("删除用户ID: {}", id);
        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        boolean deleted = userService.deleteUserById(id);
        return ResponseEntity.ok(Map.of("deleted", deleted));
    }
}

package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 * 使用MyBatis注解实现CRUD操作
 */
@Mapper
public interface UserMapper {
    
    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM user")
    List<User> findAll();
    
    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
    
    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);
    
    /**
     * 创建新用户
     */
    @Insert("INSERT INTO user(username, password, email) VALUES(#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    /**
     * 保存用户（与insert方法相同，为了与Service层方法名一致）
     */
    @Insert("INSERT INTO user(username, password, email) VALUES(#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(User user);
    
    /**
     * 更新用户信息
     */
    @Update("UPDATE user SET username = #{username}, password = #{password}, email = #{email} WHERE id = #{id}")
    int update(User user);
    
    /**
     * 根据ID删除用户
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);
}

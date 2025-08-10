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
     * 保存用户
     */
    @Insert("INSERT INTO user(username, password, email) VALUES(#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(User user);
    
    /**
     * 更新用户
     */
    @Update("UPDATE user SET username = #{username}, password = #{password}, email = #{email} WHERE id = #{id}")
    int update(User user);
    
    /**
     * 删除用户
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);
}

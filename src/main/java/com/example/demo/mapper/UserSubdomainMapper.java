package com.example.demo.mapper;

import com.example.demo.entity.UserSubdomain;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 文件名：UserSubdomainMapper.java
 * 功能：用户三级域名数据访问层接口
 * 作者：CodeBuddy
 * 创建时间：2025-08-22
 * 版本：v1.0.0
 */
@Mapper
public interface UserSubdomainMapper {
    
    /**
     * 插入用户三级域名记录
     * 
     * @param userSubdomain 用户三级域名实体
     * @return 影响行数
     */
    @Insert("INSERT INTO user_subdomain (user_id, subdomain, domain, record_id, ip_address, ttl, status) " +
            "VALUES (#{userId}, #{subdomain}, #{domain}, #{recordId}, #{ipAddress}, #{ttl}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserSubdomain userSubdomain);
    
    /**
     * 根据ID查询用户三级域名记录
     * 
     * @param id 记录ID
     * @return 用户三级域名实体
     */
    @Select("SELECT * FROM user_subdomain WHERE id = #{id}")
    UserSubdomain findById(Long id);
    
    /**
     * 根据用户ID查询用户三级域名记录列表
     * 
     * @param userId 用户ID
     * @return 用户三级域名实体列表
     */
    @Select("SELECT * FROM user_subdomain WHERE user_id = #{userId} AND status != 'DELETED' ORDER BY create_time DESC")
    List<UserSubdomain> findByUserId(Long userId);
    
    /**
     * 根据子域名和主域名查询用户三级域名记录
     * 
     * @param subdomain 子域名前缀
     * @param domain 主域名
     * @return 用户三级域名实体
     */
    @Select("SELECT * FROM user_subdomain WHERE subdomain = #{subdomain} AND domain = #{domain} AND status != 'DELETED'")
    UserSubdomain findBySubdomainAndDomain(@Param("subdomain") String subdomain, @Param("domain") String domain);
    
    /**
     * 更新用户三级域名记录状态
     * 
     * @param id 记录ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新用户三级域名记录IP地址
     * 
     * @param id 记录ID
     * @param ipAddress IP地址
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET ip_address = #{ipAddress} WHERE id = #{id}")
    int updateIpAddress(@Param("id") Long id, @Param("ipAddress") String ipAddress);
    
    /**
     * 删除用户三级域名记录（逻辑删除）
     * 
     * @param id 记录ID
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET status = 'DELETED' WHERE id = #{id}")
    int delete(Long id);
    
    /**
     * 查询用户三级域名记录总数
     * 
     * @param userId 用户ID
     * @return 记录总数
     */
    @Select("SELECT COUNT(*) FROM user_subdomain WHERE user_id = #{userId} AND status != 'DELETED'")
    int countByUserId(Long userId);
    
    /**
     * 查询所有活跃的用户三级域名记录
     * 
     * @return 用户三级域名实体列表
     */
    @Select("SELECT * FROM user_subdomain WHERE status = 'ACTIVE' ORDER BY create_time DESC")
    List<UserSubdomain> findAllActive();
    
    /**
     * 根据状态查询用户三级域名记录
     * 
     * @param status 状态
     * @return 用户三级域名实体列表
     */
    @Select("SELECT * FROM user_subdomain WHERE status = #{status} ORDER BY create_time DESC")
    List<UserSubdomain> findByStatus(String status);
}
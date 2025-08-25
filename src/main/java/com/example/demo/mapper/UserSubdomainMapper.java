package com.example.demo.mapper;

import com.example.demo.entity.UserSubdomain;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户3级域名Mapper接口
 * 
 * @author CodeBuddy
 * @since 2025-08-24
 */
@Mapper
public interface UserSubdomainMapper {
    
    /**
     * 插入用户子域名
     * 
     * @param userSubdomain 用户子域名信息
     * @return 影响行数
     */
    @Insert("INSERT INTO user_subdomain (user_id, subdomain, domain, full_domain, status, remark) " +
            "VALUES (#{userId}, #{subdomain}, #{domain}, #{fullDomain}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserSubdomain userSubdomain);
    
    /**
     * 根据ID查询用户子域名
     * 
     * @param id 主键ID
     * @return 用户子域名信息
     */
    @Select("SELECT * FROM user_subdomain WHERE id = #{id}")
    UserSubdomain selectById(Long id);
    
    /**
     * 根据用户ID查询子域名列表
     * 
     * @param userId 用户ID
     * @return 用户子域名列表
     */
    @Select("SELECT * FROM user_subdomain WHERE user_id = #{userId} AND status != 'DELETED' ORDER BY create_time DESC")
    List<UserSubdomain> selectByUserId(Long userId);
    
    /**
     * 根据完整域名查询
     * 
     * @param fullDomain 完整域名
     * @return 用户子域名信息
     */
    @Select("SELECT * FROM user_subdomain WHERE full_domain = #{fullDomain}")
    UserSubdomain selectByFullDomain(String fullDomain);
    
    /**
     * 检查域名是否已存在
     * 
     * @param fullDomain 完整域名
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM user_subdomain WHERE full_domain = #{fullDomain} AND status != 'DELETED'")
    int countByFullDomain(String fullDomain);
    
    /**
     * 更新用户子域名状态
     * 
     * @param id 主键ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新用户子域名备注
     * 
     * @param id 主键ID
     * @param remark 备注信息
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET remark = #{remark} WHERE id = #{id}")
    int updateRemark(@Param("id") Long id, @Param("remark") String remark);
    
    /**
     * 删除用户子域名（软删除）
     * 
     * @param id 主键ID
     * @return 影响行数
     */
    @Update("UPDATE user_subdomain SET status = 'DELETED' WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据用户ID和状态查询子域名列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 用户子域名列表
     */
    @Select("SELECT * FROM user_subdomain WHERE user_id = #{userId} AND status = #{status} ORDER BY create_time DESC")
    List<UserSubdomain> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    /**
     * 查询所有活跃的子域名
     * 
     * @return 活跃的用户子域名列表
     */
    @Select("SELECT * FROM user_subdomain WHERE status = 'ACTIVE' ORDER BY create_time DESC")
    List<UserSubdomain> selectAllActive();
    
    /**
     * 根据子域名和主域名查询是否已注册
     * 
     * @param subdomain 子域名前缀
     * @param domain 主域名
     * @return 查询结果
     */
    @Select("SELECT * FROM user_subdomain WHERE subdomain = #{subdomain} AND domain = #{domain} AND status != 'DELETED'")
    UserSubdomain selectBySubdomainAndDomain(@Param("subdomain") String subdomain, @Param("domain") String domain);
}
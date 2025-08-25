package com.example.demo.mapper;

import com.example.demo.entity.UserDnsRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户DNS解析记录Mapper接口
 * 提供用户DNS解析记录的数据库操作方法
 * 
 * @author CodeBuddy
 * @since 2025-08-25
 */
@Mapper
public interface UserDnsRecordMapper {
    
    /**
     * 插入用户DNS解析记录
     * 
     * @param record 用户DNS解析记录对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO user_dns_record (user_id, subdomain_id, record_id, name, type, value, line, line_id, ttl, mx, weight, status, remark, monitor_status, updated_on, sync_status, sync_error) " +
            "VALUES (#{userId}, #{subdomainId}, #{recordId}, #{name}, #{type}, #{value}, #{line}, #{lineId}, #{ttl}, #{mx}, #{weight}, #{status}, #{remark}, #{monitorStatus}, #{updatedOn}, #{syncStatus}, #{syncError})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserDnsRecord record);
    
    /**
     * 根据ID查询用户DNS解析记录
     * 
     * @param id 记录ID
     * @return 用户DNS解析记录对象
     */
    @Select("SELECT * FROM user_dns_record WHERE id = #{id}")
    UserDnsRecord selectById(Long id);
    
    /**
     * 根据用户ID查询DNS解析记录列表
     * 
     * @param userId 用户ID
     * @return DNS解析记录列表
     */
    @Select("SELECT * FROM user_dns_record WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<UserDnsRecord> selectByUserId(Long userId);
    
    /**
     * 根据子域名ID查询DNS解析记录列表
     * 
     * @param subdomainId 子域名ID
     * @return DNS解析记录列表
     */
    @Select("SELECT * FROM user_dns_record WHERE subdomain_id = #{subdomainId} ORDER BY create_time DESC")
    List<UserDnsRecord> selectBySubdomainId(Long subdomainId);
    
    /**
     * 根据用户ID和子域名ID查询DNS解析记录列表
     * 
     * @param userId 用户ID
     * @param subdomainId 子域名ID
     * @return DNS解析记录列表
     */
    @Select("SELECT * FROM user_dns_record WHERE user_id = #{userId} AND subdomain_id = #{subdomainId} ORDER BY create_time DESC")
    List<UserDnsRecord> selectByUserIdAndSubdomainId(Long userId, Long subdomainId);
    
    /**
     * 根据DNSPod记录ID查询用户DNS解析记录
     * 
     * @param recordId DNSPod记录ID
     * @return 用户DNS解析记录对象
     */
    @Select("SELECT * FROM user_dns_record WHERE record_id = #{recordId}")
    UserDnsRecord selectByRecordId(Long recordId);
    
    /**
     * 根据同步状态查询DNS解析记录列表
     * 
     * @param syncStatus 同步状态
     * @return DNS解析记录列表
     */
    @Select("SELECT * FROM user_dns_record WHERE sync_status = #{syncStatus} ORDER BY create_time ASC")
    List<UserDnsRecord> selectBySyncStatus(String syncStatus);
    
    /**
     * 更新用户DNS解析记录
     * 
     * @param record 用户DNS解析记录对象
     * @return 影响的行数
     */
    @Update("UPDATE user_dns_record SET record_id = #{recordId}, name = #{name}, type = #{type}, value = #{value}, " +
            "line = #{line}, line_id = #{lineId}, ttl = #{ttl}, mx = #{mx}, weight = #{weight}, status = #{status}, " +
            "remark = #{remark}, monitor_status = #{monitorStatus}, updated_on = #{updatedOn}, sync_status = #{syncStatus}, " +
            "sync_error = #{syncError} WHERE id = #{id}")
    int update(UserDnsRecord record);
    
    /**
     * 更新同步状态
     * 
     * @param id 记录ID
     * @param syncStatus 同步状态
     * @param syncError 同步错误信息
     * @return 影响的行数
     */
    @Update("UPDATE user_dns_record SET sync_status = #{syncStatus}, sync_error = #{syncError} WHERE id = #{id}")
    int updateSyncStatus(Long id, String syncStatus, String syncError);
    
    /**
     * 更新DNSPod记录ID
     * 
     * @param id 记录ID
     * @param recordId DNSPod记录ID
     * @return 影响的行数
     */
    @Update("UPDATE user_dns_record SET record_id = #{recordId}, sync_status = 'SUCCESS' WHERE id = #{id}")
    int updateRecordId(Long id, Long recordId);
    
    /**
     * 根据ID删除用户DNS解析记录
     * 
     * @param id 记录ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_dns_record WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据用户ID删除所有DNS解析记录
     * 
     * @param userId 用户ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_dns_record WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
    
    /**
     * 根据子域名ID删除所有DNS解析记录
     * 
     * @param subdomainId 子域名ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_dns_record WHERE subdomain_id = #{subdomainId}")
    int deleteBySubdomainId(Long subdomainId);
    
    /**
     * 统计用户的DNS解析记录数量
     * 
     * @param userId 用户ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM user_dns_record WHERE user_id = #{userId}")
    int countByUserId(Long userId);
    
    /**
     * 统计子域名的DNS解析记录数量
     * 
     * @param subdomainId 子域名ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM user_dns_record WHERE subdomain_id = #{subdomainId}")
    int countBySubdomainId(Long subdomainId);
    
    /**
     * 检查记录是否存在（用户ID + 子域名ID + 主机记录 + 记录类型）
     * 
     * @param userId 用户ID
     * @param subdomainId 子域名ID
     * @param name 主机记录
     * @param type 记录类型
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM user_dns_record WHERE user_id = #{userId} AND subdomain_id = #{subdomainId} AND name = #{name} AND type = #{type}")
    int existsByUserIdAndSubdomainIdAndNameAndType(Long userId, Long subdomainId, String name, String type);
}
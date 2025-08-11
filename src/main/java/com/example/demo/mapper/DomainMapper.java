// 文件名：DomainMapper.java
// 功能：域名信息数据访问接口，提供域名相关的数据库操作
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：使用MyBatis进行数据库操作

package com.example.demo.mapper;

import com.example.demo.entity.Domain;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 域名信息数据访问接口
 * 提供域名相关的数据库操作方法
 */
@Mapper
public interface DomainMapper {

    /**
     * 查询所有域名信息
     * @return 域名信息列表
     */
    @Select("SELECT * FROM domain_info ORDER BY create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "domainId", column = "domain_id"),
        @Result(property = "domainName", column = "domain_name"),
        @Result(property = "status", column = "status"),
        @Result(property = "grade", column = "grade"),
        @Result(property = "gradeTitle", column = "grade_title"),
        @Result(property = "isMark", column = "is_mark"),
        @Result(property = "ttl", column = "ttl"),
        @Result(property = "dnsStatus", column = "dns_status"),
        @Result(property = "minTtl", column = "min_ttl"),
        @Result(property = "recordCount", column = "record_count"),
        @Result(property = "createdOn", column = "created_on"),
        @Result(property = "updatedOn", column = "updated_on"),
        @Result(property = "owner", column = "owner"),
        @Result(property = "remark", column = "remark"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<Domain> findAll();

    /**
     * 根据域名ID查询域名信息
     * @param domainId 腾讯云域名ID
     * @return 域名信息
     */
    @Select("SELECT * FROM domain_info WHERE domain_id = #{domainId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "domainId", column = "domain_id"),
        @Result(property = "domainName", column = "domain_name"),
        @Result(property = "status", column = "status"),
        @Result(property = "grade", column = "grade"),
        @Result(property = "gradeTitle", column = "grade_title"),
        @Result(property = "isMark", column = "is_mark"),
        @Result(property = "ttl", column = "ttl"),
        @Result(property = "dnsStatus", column = "dns_status"),
        @Result(property = "minTtl", column = "min_ttl"),
        @Result(property = "recordCount", column = "record_count"),
        @Result(property = "createdOn", column = "created_on"),
        @Result(property = "updatedOn", column = "updated_on"),
        @Result(property = "owner", column = "owner"),
        @Result(property = "remark", column = "remark"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    Domain findByDomainId(Long domainId);

    /**
     * 根据域名名称查询域名信息
     * @param domainName 域名名称
     * @return 域名信息
     */
    @Select("SELECT * FROM domain_info WHERE domain_name = #{domainName}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "domainId", column = "domain_id"),
        @Result(property = "domainName", column = "domain_name"),
        @Result(property = "status", column = "status"),
        @Result(property = "grade", column = "grade"),
        @Result(property = "gradeTitle", column = "grade_title"),
        @Result(property = "isMark", column = "is_mark"),
        @Result(property = "ttl", column = "ttl"),
        @Result(property = "dnsStatus", column = "dns_status"),
        @Result(property = "minTtl", column = "min_ttl"),
        @Result(property = "recordCount", column = "record_count"),
        @Result(property = "createdOn", column = "created_on"),
        @Result(property = "updatedOn", column = "updated_on"),
        @Result(property = "owner", column = "owner"),
        @Result(property = "remark", column = "remark"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    Domain findByDomainName(String domainName);

    /**
     * 插入域名信息
     * @param domain 域名信息
     * @return 影响行数
     */
    @Insert("INSERT INTO domain_info (domain_id, domain_name, status, grade, grade_title, is_mark, ttl, dns_status, min_ttl, record_count, created_on, updated_on, owner, remark) " +
            "VALUES (#{domainId}, #{domainName}, #{status}, #{grade}, #{gradeTitle}, #{isMark}, #{ttl}, #{dnsStatus}, #{minTtl}, #{recordCount}, #{createdOn}, #{updatedOn}, #{owner}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Domain domain);

    /**
     * 更新域名信息
     * @param domain 域名信息
     * @return 影响行数
     */
    @Update("UPDATE domain_info SET domain_name = #{domainName}, status = #{status}, grade = #{grade}, grade_title = #{gradeTitle}, " +
            "is_mark = #{isMark}, ttl = #{ttl}, dns_status = #{dnsStatus}, min_ttl = #{minTtl}, record_count = #{recordCount}, " +
            "created_on = #{createdOn}, updated_on = #{updatedOn}, owner = #{owner}, remark = #{remark} " +
            "WHERE domain_id = #{domainId}")
    int updateByDomainId(Domain domain);

    /**
     * 根据域名ID删除域名信息
     * @param domainId 腾讯云域名ID
     * @return 影响行数
     */
    @Delete("DELETE FROM domain_info WHERE domain_id = #{domainId}")
    int deleteByDomainId(Long domainId);

    /**
     * 批量插入域名信息（用于数据同步）
     * @param domains 域名信息列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO domain_info (domain_id, domain_name, status, grade, grade_title, is_mark, ttl, dns_status, min_ttl, record_count, created_on, updated_on, owner, remark) VALUES " +
            "<foreach collection='list' item='domain' separator=','>" +
            "(#{domain.domainId}, #{domain.domainName}, #{domain.status}, #{domain.grade}, #{domain.gradeTitle}, #{domain.isMark}, #{domain.ttl}, #{domain.dnsStatus}, #{domain.minTtl}, #{domain.recordCount}, #{domain.createdOn}, #{domain.updatedOn}, #{domain.owner}, #{domain.remark})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<Domain> domains);
}
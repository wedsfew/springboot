// 文件名：Domain.java
// 功能：域名信息实体类，映射domain_info数据库表
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：用于存储从腾讯云DNSPod获取的域名信息

package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 域名信息实体类
 * 对应数据库表：domain_info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domain {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 腾讯云域名ID
     */
    private Long domainId;
    
    /**
     * 域名名称
     */
    private String domainName;
    
    /**
     * 域名状态：ACTIVE-正常，PAUSE-暂停，SPAM-封禁
     */
    private String status;
    
    /**
     * 域名等级：D_Free-免费版，D_Plus-个人豪华版，D_Extra-企业1号，D_Expert-企业2号，D_Ultra-企业3号
     */
    private String grade;
    
    /**
     * 域名等级标题
     */
    private String gradeTitle;
    
    /**
     * 是否星标：false-否，true-是
     */
    private Boolean isMark;
    
    /**
     * TTL值，默认600秒
     */
    private Integer ttl;
    
    /**
     * DNS状态：DNSPOD-使用DNSPod，QCLOUD-使用腾讯云解析
     */
    private String dnsStatus;
    
    /**
     * 最小TTL值
     */
    private Integer minTtl;
    
    /**
     * 记录数量
     */
    private Integer recordCount;
    
    /**
     * 域名创建时间（腾讯云）
     */
    private LocalDateTime createdOn;
    
    /**
     * 域名更新时间（腾讯云）
     */
    private LocalDateTime updatedOn;
    
    /**
     * 域名所有者
     */
    private String owner;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}
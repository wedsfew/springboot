-- 文件名：domain_schema.sql
-- 功能：域名信息表结构定义
-- 作者：系统生成
-- 创建时间：2025-01-11
-- 版本：v1.0.0
-- 备注：用于存储从腾讯云DNSPod获取的域名信息

CREATE TABLE `domain_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `domain_id` bigint NOT NULL COMMENT '腾讯云域名ID',
  `domain_name` varchar(255) NOT NULL COMMENT '域名名称',
  `status` varchar(50) NOT NULL DEFAULT 'ACTIVE' COMMENT '域名状态：ACTIVE-正常，PAUSE-暂停，SPAM-封禁',
  `grade` varchar(50) DEFAULT NULL COMMENT '域名等级：D_Free-免费版，D_Plus-个人豪华版，D_Extra-企业1号，D_Expert-企业2号，D_Ultra-企业3号',
  `grade_title` varchar(100) DEFAULT NULL COMMENT '域名等级标题',
  `is_mark` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否星标：0-否，1-是',
  `ttl` int DEFAULT 600 COMMENT 'TTL值，默认600秒',
  `dns_status` varchar(50) DEFAULT 'DNSPOD' COMMENT 'DNS状态：DNSPOD-使用DNSPod，QCLOUD-使用腾讯云解析',
  `min_ttl` int DEFAULT 1 COMMENT '最小TTL值',
  `record_count` int DEFAULT 0 COMMENT '记录数量',
  `created_on` datetime DEFAULT NULL COMMENT '域名创建时间',
  `updated_on` datetime DEFAULT NULL COMMENT '域名更新时间',
  `owner` varchar(100) DEFAULT NULL COMMENT '域名所有者',
  `remark` text COMMENT '备注信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_domain_id` (`domain_id`),
  UNIQUE KEY `uk_domain_name` (`domain_name`),
  KEY `idx_status` (`status`),
  KEY `idx_grade` (`grade`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='域名信息表';
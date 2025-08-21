-- 文件名：domain_table.sql
-- 功能：创建域名表，用于保存从DNSPod获取的域名信息
-- 作者：CodeBuddy
-- 创建时间：2025-08-21
-- 版本：v1.0.0

CREATE TABLE `domain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `domain_id` bigint NOT NULL COMMENT 'DNSPod域名ID',
  `name` varchar(255) NOT NULL COMMENT '域名名称',
  `punycode` varchar(255) NOT NULL COMMENT '域名Punycode编码',
  `grade` varchar(50) NOT NULL COMMENT '域名套餐等级',
  `grade_level` int NOT NULL DEFAULT '0' COMMENT '套餐等级数值',
  `grade_title` varchar(100) NOT NULL COMMENT '套餐等级标题',
  `is_vip` varchar(10) NOT NULL DEFAULT 'NO' COMMENT '是否为VIP域名',
  `owner` varchar(255) NOT NULL COMMENT '域名所有者',
  `status` varchar(20) NOT NULL DEFAULT 'ENABLE' COMMENT '域名状态',
  `group_id` int NOT NULL DEFAULT '1' COMMENT '分组ID',
  `search_engine_push` varchar(10) NOT NULL DEFAULT 'NO' COMMENT '搜索引擎推送状态',
  `record_count` int NOT NULL DEFAULT '0' COMMENT '解析记录数量',
  `ttl` int NOT NULL DEFAULT '600' COMMENT '默认TTL值',
  `cname_speedup` varchar(20) NOT NULL DEFAULT 'DISABLE' COMMENT 'CNAME加速状态',
  `dns_status` varchar(50) DEFAULT '' COMMENT 'DNS状态',
  `remark` text COMMENT '域名备注',
  `vip_start_at` datetime DEFAULT NULL COMMENT 'VIP开始时间',
  `vip_end_at` datetime DEFAULT NULL COMMENT 'VIP结束时间',
  `vip_auto_renew` varchar(10) NOT NULL DEFAULT 'NO' COMMENT 'VIP自动续费状态',
  `created_on` datetime NOT NULL COMMENT 'DNSPod创建时间',
  `updated_on` datetime NOT NULL COMMENT 'DNSPod更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本地创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '本地更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `domain_id` (`domain_id`),
  UNIQUE KEY `name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_owner` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='域名信息表';
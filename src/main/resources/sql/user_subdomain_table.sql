-- 用户三级域名表
-- 用于存储用户注册的三级域名信息
-- 作者：CodeBuddy
-- 创建时间：2025-08-22

USE `demo`;

CREATE TABLE `user_subdomain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `subdomain` varchar(100) NOT NULL COMMENT '子域名前缀',
  `domain` varchar(100) NOT NULL COMMENT '主域名',
  `record_id` bigint NOT NULL COMMENT 'DNSPod记录ID',
  `ip_address` varchar(100) NOT NULL COMMENT 'IP地址',
  `ttl` int NOT NULL DEFAULT '600' COMMENT 'TTL值',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-不活跃，DELETED-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_subdomain` (`user_id`, `subdomain`, `domain`),
  UNIQUE KEY `uk_subdomain_domain` (`subdomain`, `domain`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户三级域名表';
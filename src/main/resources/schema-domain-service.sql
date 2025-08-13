-- 免费3级域名服务数据库表结构
-- 文件名：schema-domain-service.sql
-- 功能：支持免费3级域名申请和DNS解析服务的数据库表
-- 作者：CodeBuddy
-- 创建时间：2025-08-12
-- 版本：v1.0.0

-- 1. 域名管理表 (domains)
-- 用于管理可分配的3级域名
CREATE TABLE `domains` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `domain_name` varchar(100) NOT NULL COMMENT '域名名称，如 cblog.eu',
  `domain_id` varchar(50) NOT NULL COMMENT 'DNSPod中的域名ID',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '域名状态：ACTIVE-活跃, INACTIVE-停用',
  `max_subdomains` int NOT NULL DEFAULT 1000 COMMENT '最大可分配子域名数量',
  `used_subdomains` int NOT NULL DEFAULT 0 COMMENT '已分配子域名数量',
  `description` text COMMENT '域名描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `domain_name` (`domain_name`),
  UNIQUE KEY `domain_id` (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='域名管理表';

-- 2. 用户域名表 (user_domains)
-- 用于管理用户申请的3级域名
CREATE TABLE `user_domains` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `domain_id` bigint NOT NULL COMMENT '主域名ID',
  `subdomain` varchar(50) NOT NULL COMMENT '子域名前缀，如 a',
  `full_domain` varchar(150) NOT NULL COMMENT '完整域名，如 a.cblog.eu',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃, SUSPENDED-暂停, EXPIRED-过期',
  `expire_time` timestamp NULL COMMENT '过期时间，NULL表示永不过期',
  `description` varchar(255) COMMENT '域名用途描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `full_domain` (`full_domain`),
  UNIQUE KEY `subdomain_domain` (`subdomain`, `domain_id`),
  KEY `user_id` (`user_id`),
  KEY `domain_id` (`domain_id`),
  CONSTRAINT `fk_user_domains_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_domains_domain` FOREIGN KEY (`domain_id`) REFERENCES `domains` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户域名表';

-- 3. DNS记录表 (dns_records)
-- 用于管理用户域名的DNS解析记录
CREATE TABLE `dns_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_domain_id` bigint NOT NULL COMMENT '用户域名ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `record_id` varchar(50) COMMENT 'DNSPod中的记录ID',
  `record_type` varchar(10) NOT NULL COMMENT '记录类型：A, AAAA, CNAME, MX, TXT等',
  `sub_domain` varchar(100) NOT NULL DEFAULT '@' COMMENT '子域名，@表示根域名',
  `record_line` varchar(50) NOT NULL DEFAULT '默认' COMMENT '解析线路',
  `value` varchar(500) NOT NULL COMMENT '记录值',
  `ttl` int NOT NULL DEFAULT 600 COMMENT 'TTL值，600-86400秒',
  `mx_priority` int DEFAULT NULL COMMENT 'MX记录优先级',
  `status` varchar(20) NOT NULL DEFAULT 'ENABLE' COMMENT '记录状态：ENABLE-启用, DISABLE-禁用',
  `remark` varchar(255) COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_domain_id` (`user_domain_id`),
  KEY `user_id` (`user_id`),
  KEY `record_id` (`record_id`),
  CONSTRAINT `fk_dns_records_user_domain` FOREIGN KEY (`user_domain_id`) REFERENCES `user_domains` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_dns_records_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='DNS记录表';

-- 4. 域名申请记录表 (domain_applications)
-- 用于记录用户的域名申请历史
CREATE TABLE `domain_applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `requested_subdomain` varchar(50) NOT NULL COMMENT '申请的子域名',
  `domain_id` bigint NOT NULL COMMENT '主域名ID',
  `full_domain` varchar(150) NOT NULL COMMENT '完整域名',
  `application_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '申请状态：PENDING-待审核, APPROVED-已批准, REJECTED-已拒绝',
  `rejection_reason` varchar(255) COMMENT '拒绝原因',
  `admin_id` bigint COMMENT '处理管理员ID',
  `processed_time` timestamp NULL COMMENT '处理时间',
  `description` varchar(255) COMMENT '申请说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `domain_id` (`domain_id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `fk_domain_applications_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_domain_applications_domain` FOREIGN KEY (`domain_id`) REFERENCES `domains` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_domain_applications_admin` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='域名申请记录表';

-- 5. 用户配额表 (user_quotas)
-- 用于管理用户的资源配额
CREATE TABLE `user_quotas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `max_domains` int NOT NULL DEFAULT 1 COMMENT '最大域名数量',
  `used_domains` int NOT NULL DEFAULT 0 COMMENT '已使用域名数量',
  `max_dns_records` int NOT NULL DEFAULT 10 COMMENT '每个域名最大DNS记录数',
  `quota_type` varchar(20) NOT NULL DEFAULT 'FREE' COMMENT '配额类型：FREE-免费, PREMIUM-付费',
  `expire_time` timestamp NULL COMMENT '配额过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `fk_user_quotas_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户配额表';

-- 6. 操作日志表 (operation_logs)
-- 用于记录重要操作的审计日志
CREATE TABLE `operation_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '操作用户ID',
  `admin_id` bigint COMMENT '操作管理员ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型：CREATE_DOMAIN, DELETE_DOMAIN, CREATE_RECORD等',
  `resource_type` varchar(50) NOT NULL COMMENT '资源类型：DOMAIN, DNS_RECORD',
  `resource_id` varchar(50) COMMENT '资源ID',
  `operation_detail` text COMMENT '操作详情JSON',
  `ip_address` varchar(45) COMMENT '操作IP地址',
  `user_agent` varchar(500) COMMENT '用户代理',
  `result` varchar(20) NOT NULL COMMENT '操作结果：SUCCESS, FAILED',
  `error_message` text COMMENT '错误信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `admin_id` (`admin_id`),
  KEY `operation_type` (`operation_type`),
  KEY `create_time` (`create_time`),
  CONSTRAINT `fk_operation_logs_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_operation_logs_admin` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';

-- 初始化数据
-- 插入主域名 cblog.eu (需要替换为实际的DNSPod域名ID)
INSERT INTO `domains` (`domain_name`, `domain_id`, `status`, `max_subdomains`, `description`) 
VALUES ('cblog.eu', 'your_dnspod_domain_id_here', 'ACTIVE', 10000, '免费3级域名服务主域名');

-- 为现有用户创建默认配额
INSERT INTO `user_quotas` (`user_id`, `max_domains`, `max_dns_records`, `quota_type`) 
SELECT `id`, 1, 10, 'FREE' FROM `user`;
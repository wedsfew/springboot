-- 用户3级域名表
CREATE TABLE `user_subdomain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `subdomain` varchar(100) NOT NULL COMMENT '子域名前缀',
  `domain` varchar(100) NOT NULL COMMENT '主域名',
  `full_domain` varchar(255) NOT NULL COMMENT '完整域名',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-不活跃，DELETED-已删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_full_domain` (`full_domain`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_domain` (`domain`),
  CONSTRAINT `fk_user_subdomain_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户3级域名表';
-- 文件名：add_missing_columns.sql
-- 功能：向用户表添加缺失的字段（role, create_time, update_time）
-- 作者：CodeBuddy
-- 创建时间：2025-08-11
-- 版本：v1.0.0

-- 添加role字段
ALTER TABLE `user` ADD COLUMN `role` varchar(20) NOT NULL DEFAULT 'USER' AFTER `email`;

-- 添加create_time字段
ALTER TABLE `user` ADD COLUMN `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `role`;

-- 添加update_time字段
ALTER TABLE `user` ADD COLUMN `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`;

-- 确认修改后的表结构
SHOW CREATE TABLE `user`;
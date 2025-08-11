-- 文件名：modify_user_table.sql
-- 功能：修改用户表，删除username的唯一键约束
-- 作者：CodeBuddy
-- 创建时间：2025-08-11
-- 版本：v1.0.0

-- 删除username的唯一键约束
ALTER TABLE `user` DROP INDEX `username`;

-- 确认修改后的表结构
SHOW CREATE TABLE `user`;
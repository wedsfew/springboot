DROP TABLE IF EXISTS user;

-- 创建用户表
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `email` (`email`)
);

-- 创建管理员表
CREATE TABLE admin (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);

-- 插入测试数据
INSERT INTO user (username, password, email) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTBpKLt2YmZIQNFmNgxdxPq0dWdUkJIy', 'admin@example.com'),
('user1', '$2a$10$8CFFt.FeopdF3jCQUwCkd.eCDmr2K0aeKgFnQtLgEccgyL4.luoFq', 'user1@example.com');

-- 插入管理员测试数据
INSERT INTO admin (username, password, email) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTBpKLt2YmZIQNFmNgxdxPq0dWdUkJIy', 'admin@admin.com');

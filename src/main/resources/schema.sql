-- 如果表已存在，先删除
DROP TABLE IF EXISTS user;

-- 创建用户表
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100)
);

-- 插入测试数据
INSERT INTO user (username, password, email) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTBpKLt2YmZIQNFmNgxdxPq0dWdUkJIy', 'admin@example.com'),
('user1', '$2a$10$8CFFt.FeopdF3jCQUwCkd.eCDmr2K0aeKgFnQtLgEccgyL4.luoFq', 'user1@example.com');
#!/bin/bash

# 获取JWT Token脚本
# 用于测试需要认证的API接口

# 服务器地址
BASE_URL="http://localhost:8080"

# 登录接口
LOGIN_URL="$BASE_URL/api/auth/login"

# 测试用户凭据（根据文档中的测试用例）
EMAIL="12345678@example.com"
PASSWORD="12345678"

echo "正在获取JWT Token..."
echo "登录邮箱: $EMAIL"
echo "请求地址: $LOGIN_URL"
echo ""

# 发送登录请求
RESPONSE=$(curl -s -X POST "$LOGIN_URL" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$EMAIL\",
    \"password\": \"$PASSWORD\"
  }")

echo "服务器响应:"
echo "$RESPONSE" | jq '.' 2>/dev/null || echo "$RESPONSE"
echo ""

# 提取token
TOKEN=$(echo "$RESPONSE" | jq -r '.data.token' 2>/dev/null)

if [ "$TOKEN" != "null" ] && [ "$TOKEN" != "" ]; then
    echo "✅ 登录成功！"
    echo "JWT Token: $TOKEN"
    echo ""
    echo "使用示例:"
    echo "curl -H \"Authorization: Bearer $TOKEN\" http://localhost:8080/api/user/subdomains/list/mine"
    echo ""
    
    # 保存token到文件
    echo "$TOKEN" > token.txt
    echo "Token已保存到 token.txt 文件"
else
    echo "❌ 登录失败，请检查用户凭据或服务器状态"
    echo "错误信息: $(echo "$RESPONSE" | jq -r '.message' 2>/dev/null || echo "解析响应失败")"
fi
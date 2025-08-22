#!/bin/bash

# 测试用户注册三级域名接口（JSON格式）
# 作者：CodeBuddy
# 创建时间：2025-08-22

# 配置参数
API_BASE_URL="http://localhost:8080"
LOGIN_ENDPOINT="/api/auth/login"
REGISTER_SUBDOMAIN_ENDPOINT="/api/user/domains/register"

# 测试用户凭据 - 使用正确的测试数据
TEST_EMAIL="12345678@example.com"
TEST_PASSWORD="12345678"

# 测试域名参数
SUBDOMAIN="test$(date +%s)" # 使用时间戳确保唯一性
IP_ADDRESS="8.8.8.8"

# 步骤1: 登录获取JWT令牌
echo "步骤1: 登录获取JWT令牌"
LOGIN_RESPONSE=$(curl -s -X POST "${API_BASE_URL}${LOGIN_ENDPOINT}" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"${TEST_EMAIL}\",\"password\":\"${TEST_PASSWORD}\"}")

echo "登录响应: ${LOGIN_RESPONSE}"

# 直接从响应中提取token字段
JWT_TOKEN=$(echo $LOGIN_RESPONSE | sed 's/.*"token":"\([^"]*\)".*/\1/')
echo "JWT令牌: ${JWT_TOKEN:0:20}..."

if [ -z "$JWT_TOKEN" ] || [ "$JWT_TOKEN" = "$LOGIN_RESPONSE" ]; then
  echo "错误: 无法提取JWT令牌"
  exit 1
fi

# 步骤2: 注册三级域名
echo "步骤2: 注册三级域名 ${SUBDOMAIN}.cblog.eu"
REGISTER_RESPONSE=$(curl -s -X POST "${API_BASE_URL}${REGISTER_SUBDOMAIN_ENDPOINT}" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{
    \"subDomain\": \"${SUBDOMAIN}\",
    \"value\": \"${IP_ADDRESS}\"
  }")

echo "注册响应: ${REGISTER_RESPONSE}"

# 步骤3: 验证域名是否已注册
echo -e "\n步骤3: 验证域名是否已注册"
CHECK_RESPONSE=$(curl -s -X GET "${API_BASE_URL}/api/dnspod/available-subdomain?subDomain=${SUBDOMAIN}" \
  -H "Authorization: Bearer ${JWT_TOKEN}")

echo "验证响应: ${CHECK_RESPONSE}"

echo -e "\n测试完成"
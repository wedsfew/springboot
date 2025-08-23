#!/bin/bash

# 测试用户注册三级域名接口
# 作者：CodeBuddy
# 创建时间：2025-08-23

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置参数
API_BASE_URL="http://localhost:8080"
LOGIN_ENDPOINT="/api/auth/login"
REGISTER_SUBDOMAIN_ENDPOINT="/api/user/domains/register"

# 测试用户凭据
TEST_EMAIL="allnotice@qq.com"
TEST_PASSWORD="12345678"

# 测试域名参数
SUBDOMAIN="34d" # 使用指定的子域名
IP_ADDRESS="8.8.8.8"
TTL=600

echo -e "${BLUE}=== 用户注册三级域名接口测试 ===${NC}"
echo -e "${YELLOW}测试时间: $(date)${NC}"
echo -e "${YELLOW}测试子域名: ${SUBDOMAIN}${NC}"

# 步骤1: 登录获取JWT令牌
echo -e "\n${BLUE}步骤1: 登录获取JWT令牌${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "${API_BASE_URL}${LOGIN_ENDPOINT}" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"${TEST_EMAIL}\",\"password\":\"${TEST_PASSWORD}\"}")

echo "登录响应: $LOGIN_RESPONSE"

# 手动设置JWT令牌 - 从响应中复制
JWT_TOKEN=$(echo $LOGIN_RESPONSE | sed 's/.*"token" : "\([^"]*\)".*/\1/')

if [ -n "$JWT_TOKEN" ]; then
  echo -e "${GREEN}登录成功!${NC}"
  echo -e "${GREEN}获取到JWT令牌: ${JWT_TOKEN:0:20}...${NC}"
else
  echo -e "${RED}登录失败! 无法获取JWT令牌${NC}"
  exit 1
fi

# 步骤2: 注册三级域名
echo -e "\n${BLUE}步骤2: 注册三级域名${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "${API_BASE_URL}${REGISTER_SUBDOMAIN_ENDPOINT}" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{
    \"subDomain\": \"${SUBDOMAIN}\",
    \"value\": \"${IP_ADDRESS}\",
    \"ttl\": ${TTL}
  }")

echo "注册响应: $REGISTER_RESPONSE"

# 检查注册是否成功
if [[ $REGISTER_RESPONSE == *"\"code\":200"* || $REGISTER_RESPONSE == *"\"code\" : 200"* ]]; then
  echo -e "${GREEN}三级域名注册成功!${NC}"
  
  # 提取记录ID
  RECORD_ID=$(echo $REGISTER_RESPONSE | sed -n 's/.*"recordId" *: *\([0-9]*\).*/\1/p')
  if [ -n "$RECORD_ID" ]; then
    echo -e "${GREEN}记录ID: ${RECORD_ID}${NC}"
  fi
else
  echo -e "${RED}三级域名注册失败!${NC}"
fi

# 步骤3: 验证域名是否已注册（通过查询接口）
echo -e "\n${BLUE}步骤3: 验证域名是否已注册${NC}"
CHECK_RESPONSE=$(curl -s -X GET "${API_BASE_URL}/api/dnspod/available-subdomain?subDomain=${SUBDOMAIN}" \
  -H "Authorization: Bearer ${JWT_TOKEN}")

echo "验证响应: $CHECK_RESPONSE"

# 检查验证结果
if [[ $CHECK_RESPONSE == *"\"code\":409"* || $CHECK_RESPONSE == *"\"code\" : 409"* ]]; then
  echo -e "${GREEN}验证成功! 域名已被注册${NC}"
else
  echo -e "${RED}验证失败! 域名应该已被注册但显示为可用。${NC}"
fi

echo -e "\n${BLUE}=== 测试完成 ===${NC}"
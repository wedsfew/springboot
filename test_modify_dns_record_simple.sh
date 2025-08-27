#!/bin/bash

# 用户修改3级域名解析记录接口简单测试脚本
# 作者: CodeBuddy
# 日期: 2025-08-25

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置参数
BASE_URL="http://localhost:8080"
API_PATH="/api/user/dns-records"
JWT_TOKEN=""
TEST_RECORD_ID=""

# 用户登录获取JWT令牌
echo -e "${BLUE}[步骤1]${NC} 登录获取JWT令牌..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "12345678@example.com",
    "password": "12345678"
  }')

# 保存登录响应以便调试
echo "$LOGIN_RESPONSE" > login_response.json

# 提取JWT令牌 - 修改提取方式以匹配实际响应格式
JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token" : "[^"]*"' | cut -d'"' -f4)

if [ -z "$JWT_TOKEN" ]; then
  echo -e "${RED}[错误]${NC} 登录失败，无法获取JWT令牌"
  echo "$LOGIN_RESPONSE"
  exit 1
else
  echo -e "${GREEN}[成功]${NC} 成功获取JWT令牌"
  echo "令牌: ${JWT_TOKEN:0:20}...（已截断）"
fi

# 获取用户DNS记录列表
echo -e "${BLUE}[步骤2]${NC} 获取用户DNS记录列表..."
RECORDS_RESPONSE=$(curl -s -X GET "$BASE_URL$API_PATH" \
  -H "Authorization: Bearer $JWT_TOKEN")

# 保存响应以便调试
echo "$RECORDS_RESPONSE" > dns_records_list.json
echo -e "${BLUE}[响应]${NC} 已保存DNS记录列表到dns_records_list.json"

# 提取第一条记录的ID用于测试
TEST_RECORD_ID=$(echo "$RECORDS_RESPONSE" | grep -o '"id" : [0-9]*' | head -1 | cut -d' ' -f3)

if [ -z "$TEST_RECORD_ID" ]; then
  echo -e "${YELLOW}[警告]${NC} 未找到可用的DNS记录，将使用默认ID 1 进行测试"
  TEST_RECORD_ID=1
else
  echo -e "${GREEN}[成功]${NC} 获取到DNS记录ID: $TEST_RECORD_ID"
fi

# 测试修改DNS记录
echo -e "${BLUE}[步骤3]${NC} 修改DNS记录..."
MODIFY_RESPONSE=$(curl -s -X PUT "$BASE_URL$API_PATH/$TEST_RECORD_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "value": "192.168.1.2",
    "ttl": 600,
    "remark": "通过简单测试脚本更新的记录"
  }')

# 保存响应以便调试
echo "$MODIFY_RESPONSE" > modify_response.json

# 检查响应
SUCCESS_CODE=$(echo "$MODIFY_RESPONSE" | grep -o '"code" : 200')

if [ -n "$SUCCESS_CODE" ]; then
  echo -e "${GREEN}[成功]${NC} DNS记录修改成功!"
  echo -e "${BLUE}[响应]${NC} $MODIFY_RESPONSE"
else
  echo -e "${RED}[失败]${NC} DNS记录修改失败"
  echo -e "${BLUE}[响应]${NC} $MODIFY_RESPONSE"
fi

echo -e "${BLUE}[测试完成]${NC}"
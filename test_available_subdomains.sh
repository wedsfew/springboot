#!/bin/bash

# 测试查询三级域名是否可用的API
# 用法: ./test_available_subdomains.sh <subdomain> [domain] [token]

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# 检查参数
if [ -z "$1" ]; then
  echo -e "${RED}错误: 请提供要查询的三级域名前缀${NC}"
  echo "用法: ./test_available_subdomains.sh <subdomain> [domain] [token]"
  echo "示例: ./test_available_subdomains.sh test"
  echo "示例: ./test_available_subdomains.sh test cblog.eu eyJhbGciOiJIUzI1NiJ9..."
  exit 1
fi

# 设置参数
SUBDOMAIN=$1
DOMAIN=${2:-"cblog.eu"}  # 如果未提供域名，默认使用cblog.eu
TOKEN=${3:-"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjI1MDYwMDAwLCJleHAiOjE2MjUxNDY0MDB9.example-token"}  # 默认令牌

echo -e "${YELLOW}正在查询三级域名 ${SUBDOMAIN}.${DOMAIN} 是否可用...${NC}"

# 发送请求查询三级域名是否可用
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "http://localhost:8080/api/dnspod/available-subdomain?subDomain=$SUBDOMAIN&domain=$DOMAIN" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN")

# 提取HTTP状态码和响应体
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

# 输出结果
echo -e "${YELLOW}HTTP状态码: ${HTTP_CODE}${NC}"
echo -e "${YELLOW}响应内容:${NC}"
echo "$BODY" | jq . 2>/dev/null || echo "$BODY"

# 检查是否包含特定消息来判断结果
if [ "$HTTP_CODE" -eq 200 ]; then
  if echo "$BODY" | grep -q "已被注册"; then
    echo -e "${RED}✗ 域名已被注册${NC}"
    exit 1
  elif echo "$BODY" | grep -q "可用"; then
    echo -e "${GREEN}✓ 域名可用，未被注册${NC}"
    exit 0
  else
    echo -e "${YELLOW}? 无法确定域名状态，请检查响应内容${NC}"
    exit 3
  fi
else
  echo -e "${RED}! 查询失败，请检查API服务是否正常运行${NC}"
  exit 2
fi

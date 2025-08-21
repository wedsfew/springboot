#!/bin/bash

# 批量测试多个三级域名是否可用
# 用法: ./test_batch_subdomains.sh domain.txt
# domain.txt 文件中每行包含一个要测试的三级域名前缀

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# 检查参数
if [ -z "$1" ] || [ ! -f "$1" ]; then
  echo -e "${RED}错误: 请提供包含域名列表的文件${NC}"
  echo "用法: ./test_batch_subdomains.sh domain.txt"
  echo "domain.txt 文件中每行包含一个要测试的三级域名前缀"
  exit 1
fi

# 设置参数
DOMAIN_FILE=$1
DOMAIN=${2:-"cblog.eu"}  # 如果未提供域名，默认使用cblog.eu
TOKEN=${3:-"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjI1MDYwMDAwLCJleHAiOjE2MjUxNDY0MDB9.example-token"}  # 默认令牌

# 统计变量
TOTAL=0
AVAILABLE=0
REGISTERED=0
FAILED=0

echo -e "${YELLOW}开始批量检查三级域名可用性...${NC}"
echo -e "${YELLOW}主域名: ${DOMAIN}${NC}"
echo "----------------------------------------"

# 读取文件中的每一行
while IFS= read -r SUBDOMAIN || [ -n "$SUBDOMAIN" ]; do
  # 跳过空行和注释行
  [[ -z "$SUBDOMAIN" || "$SUBDOMAIN" =~ ^# ]] && continue
  
  TOTAL=$((TOTAL+1))
  echo -e "${YELLOW}[$TOTAL] 检查: ${SUBDOMAIN}.${DOMAIN}${NC}"
  
  # 发送请求查询三级域名是否可用
  RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "http://localhost:8080/api/dnspod/available-subdomain?subDomain=$SUBDOMAIN&domain=$DOMAIN" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

  # 提取HTTP状态码和响应体
  HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
  BODY=$(echo "$RESPONSE" | sed '$d')

  # 检查是否包含特定消息来判断结果
  if [ "$HTTP_CODE" -eq 200 ]; then
    if echo "$BODY" | grep -q "已被注册"; then
      echo -e "${RED}✗ 域名已被注册${NC}"
      REGISTERED=$((REGISTERED+1))
    elif echo "$BODY" | grep -q "可用"; then
      echo -e "${GREEN}✓ 域名可用，未被注册${NC}"
      AVAILABLE=$((AVAILABLE+1))
    else
      echo -e "${YELLOW}? 无法确定域名状态${NC}"
      FAILED=$((FAILED+1))
    fi
  else
    echo -e "${RED}! 查询失败，HTTP状态码: ${HTTP_CODE}${NC}"
    FAILED=$((FAILED+1))
  fi
  
  echo "----------------------------------------"
  
done < "$DOMAIN_FILE"

# 输出统计结果
echo -e "${YELLOW}批量检查完成，统计结果:${NC}"
echo -e "总计检查: ${TOTAL} 个域名"
echo -e "${GREEN}可用域名: ${AVAILABLE} 个${NC}"
echo -e "${RED}已注册域名: ${REGISTERED} 个${NC}"
echo -e "${YELLOW}查询失败: ${FAILED} 个${NC}"
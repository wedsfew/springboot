#!/bin/bash

# 获取JWT令牌
echo "获取JWT令牌..."
TOKEN_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" \
  -d '{"email":"12345678@example.com","password":"12345678"}' \
  http://localhost:8080/api/auth/login)

echo "登录响应: $TOKEN_RESPONSE"
TOKEN=$(echo "$TOKEN_RESPONSE" | grep -o '"token"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/.*"token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/')

echo "JWT令牌: $TOKEN"

# 测试添加域名解析记录接口（JSON格式）
echo -e "\n测试添加域名解析记录接口（JSON格式）..."
CREATE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/dnspod/records" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "domain": "cblog.eu",
    "recordType": "A",
    "value": "8.8.8.8",
    "subDomain": "json-test",
    "ttl": 600,
    "remark": "JSON测试A记录"
  }')

echo "$CREATE_RESPONSE"
RECORD_ID=$(echo "$CREATE_RESPONSE" | grep -o '"recordId" *: *[0-9]*' | grep -o '[0-9]*$')
echo "创建的记录ID: $RECORD_ID"

if [ -n "$RECORD_ID" ]; then
  # 测试修改域名解析记录接口（JSON格式）
  echo -e "\n测试修改域名解析记录接口（JSON格式）..."
  MODIFY_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/dnspod/records/modify" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"domain\": \"cblog.eu\",
      \"recordId\": $RECORD_ID,
      \"recordType\": \"A\",
      \"value\": \"8.8.8.9\",
      \"subDomain\": \"json-test\",
      \"ttl\": 600,
      \"remark\": \"修改后的JSON测试A记录\"
    }")
  
  echo "$MODIFY_RESPONSE"
  
  # 测试删除域名解析记录接口（JSON格式）
  echo -e "\n测试删除域名解析记录接口（JSON格式）..."
  DELETE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/dnspod/records/delete" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"domain\": \"cblog.eu\",
      \"recordId\": $RECORD_ID
    }")
  
  echo "$DELETE_RESPONSE"
else
  echo "创建记录失败，无法继续测试"
fi
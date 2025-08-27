#!/bin/bash

# 测试添加有效的DNS记录脚本
# 根据接口文档和项目规则创建

echo "=== 测试添加有效的DNS记录 ==="

# 配置信息
BASE_URL="http://localhost:8080"
LOGIN_URL="$BASE_URL/api/auth/login"
DNS_RECORD_URL="$BASE_URL/api/user/dns-records"
SUBDOMAIN_LIST_URL="$BASE_URL/api/user/subdomains/list/mine"

# 测试用户信息（使用测试数据中的用户）
EMAIL="12345678@example.com"
PASSWORD="12345678"

echo "1. 用户登录获取JWT令牌..."
LOGIN_RESPONSE=$(curl -s -X POST "$LOGIN_URL" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$EMAIL\",
    \"password\": \"$PASSWORD\"
  }")

echo "登录响应: $LOGIN_RESPONSE"

# 提取JWT令牌 - 使用更灵活的方式
JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | sed -n 's/.*"token" : "\([^"]*\)".*/\1/p')

if [ -z "$JWT_TOKEN" ]; then
    echo "❌ 登录失败，无法获取JWT令牌"
    exit 1
fi

echo "✅ 登录成功，JWT令牌: ${JWT_TOKEN:0:20}..."

echo ""
echo "2. 获取用户域名列表..."
SUBDOMAIN_RESPONSE=$(curl -s -X GET "$SUBDOMAIN_LIST_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json")

echo "域名列表响应: $SUBDOMAIN_RESPONSE"

# 提取第一个子域名ID - 使用更灵活的方式
SUBDOMAIN_ID=$(echo "$SUBDOMAIN_RESPONSE" | sed -n 's/.*"id" : \([0-9]*\).*/\1/p' | head -1)

if [ -z "$SUBDOMAIN_ID" ]; then
    echo "❌ 未找到可用的子域名"
    exit 1
fi

echo "✅ 找到子域名ID: $SUBDOMAIN_ID"

echo ""
echo "3. 测试添加A记录..."

# 测试用例1: 添加A记录
echo "测试用例1: 添加A记录"
A_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"www\",
    \"type\": \"A\",
    \"value\": \"192.168.1.100\",
    \"ttl\": 600,
    \"remark\": \"测试A记录\"
  }")

echo "A记录添加响应: $A_RECORD_RESPONSE"

# 检查是否成功
if echo "$A_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ A记录添加成功"
else
    echo "❌ A记录添加失败"
fi

echo ""
echo "4. 测试添加CNAME记录..."

# 测试用例2: 添加CNAME记录
echo "测试用例2: 添加CNAME记录"
CNAME_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"blog\",
    \"type\": \"CNAME\",
    \"value\": \"example.com\",
    \"ttl\": 600,
    \"remark\": \"测试CNAME记录\"
  }")

echo "CNAME记录添加响应: $CNAME_RECORD_RESPONSE"

# 检查是否成功
if echo "$CNAME_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ CNAME记录添加成功"
else
    echo "❌ CNAME记录添加失败"
fi

echo ""
echo "5. 测试添加MX记录..."

# 测试用例3: 添加MX记录
echo "测试用例3: 添加MX记录"
MX_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"@\",
    \"type\": \"MX\",
    \"value\": \"mail.example.com\",
    \"mx\": 10,
    \"ttl\": 600,
    \"remark\": \"测试MX记录\"
  }")

echo "MX记录添加响应: $MX_RECORD_RESPONSE"

# 检查是否成功
if echo "$MX_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ MX记录添加成功"
else
    echo "❌ MX记录添加失败"
fi

echo ""
echo "6. 测试添加TXT记录..."

# 测试用例4: 添加TXT记录
echo "测试用例4: 添加TXT记录"
TXT_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"@\",
    \"type\": \"TXT\",
    \"value\": \"v=spf1 include:_spf.example.com ~all\",
    \"ttl\": 600,
    \"remark\": \"测试TXT记录\"
  }")

echo "TXT记录添加响应: $TXT_RECORD_RESPONSE"

# 检查是否成功
if echo "$TXT_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ TXT记录添加成功"
else
    echo "❌ TXT记录添加失败"
fi

echo ""
echo "7. 获取DNS记录列表验证..."

# 获取DNS记录列表
DNS_LIST_RESPONSE=$(curl -s -X GET "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json")

echo "DNS记录列表: $DNS_LIST_RESPONSE"

echo ""
echo "=== 测试完成 ==="

# 统计结果
SUCCESS_COUNT=0
TOTAL_COUNT=4

if echo "$A_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

if echo "$CNAME_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

if echo "$MX_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

if echo "$TXT_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

echo "测试结果: $SUCCESS_COUNT/$TOTAL_COUNT 个测试用例通过"

if [ $SUCCESS_COUNT -eq $TOTAL_COUNT ]; then
    echo "🎉 所有测试用例都通过了！"
    exit 0
else
    echo "⚠️  部分测试用例失败，请检查日志"
    exit 1
fi
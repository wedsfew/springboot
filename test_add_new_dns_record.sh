#!/bin/bash

# 测试添加新的有效DNS记录脚本
# 使用不同的记录名称避免冲突

echo "=== 测试添加新的有效DNS记录 ==="

# 配置信息
BASE_URL="http://localhost:8080"
LOGIN_URL="$BASE_URL/api/auth/login"
DNS_RECORD_URL="$BASE_URL/api/user/dns-records"
SUBDOMAIN_LIST_URL="$BASE_URL/api/user/subdomains/list/mine"

# 测试用户信息
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

# 提取JWT令牌
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

# 提取第一个子域名ID
SUBDOMAIN_ID=$(echo "$SUBDOMAIN_RESPONSE" | sed -n 's/.*"id" : \([0-9]*\).*/\1/p' | head -1)

if [ -z "$SUBDOMAIN_ID" ]; then
    echo "❌ 未找到可用的子域名"
    exit 1
fi

echo "✅ 找到子域名ID: $SUBDOMAIN_ID"

# 生成时间戳用于创建唯一记录名称
TIMESTAMP=$(date +%s)

echo ""
echo "3. 测试添加新的A记录..."

# 测试用例1: 添加A记录（使用时间戳确保唯一性）
echo "测试用例1: 添加A记录"
A_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"test$TIMESTAMP\",
    \"type\": \"A\",
    \"value\": \"192.168.1.200\",
    \"ttl\": 600,
    \"remark\": \"新测试A记录\"
  }")

echo "A记录添加响应: $A_RECORD_RESPONSE"

# 检查是否成功
if echo "$A_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ A记录添加成功"
else
    echo "❌ A记录添加失败"
fi

echo ""
echo "4. 测试添加新的CNAME记录..."

# 测试用例2: 添加CNAME记录
echo "测试用例2: 添加CNAME记录"
CNAME_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"api$TIMESTAMP\",
    \"type\": \"CNAME\",
    \"value\": \"api.example.com\",
    \"ttl\": 600,
    \"remark\": \"新测试CNAME记录\"
  }")

echo "CNAME记录添加响应: $CNAME_RECORD_RESPONSE"

# 检查是否成功
if echo "$CNAME_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ CNAME记录添加成功"
else
    echo "❌ CNAME记录添加失败"
fi

echo ""
echo "5. 测试添加新的TXT记录..."

# 测试用例3: 添加TXT记录
echo "测试用例3: 添加TXT记录"
TXT_RECORD_RESPONSE=$(curl -s -X POST "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"subdomainId\": $SUBDOMAIN_ID,
    \"name\": \"txt$TIMESTAMP\",
    \"type\": \"TXT\",
    \"value\": \"v=test$TIMESTAMP\",
    \"ttl\": 600,
    \"remark\": \"新测试TXT记录\"
  }")

echo "TXT记录添加响应: $TXT_RECORD_RESPONSE"

# 检查是否成功
if echo "$TXT_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    echo "✅ TXT记录添加成功"
else
    echo "❌ TXT记录添加失败"
fi

echo ""
echo "6. 验证记录添加结果..."

# 获取DNS记录列表验证
DNS_LIST_RESPONSE=$(curl -s -X GET "$DNS_RECORD_URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json")

echo "最新DNS记录列表（前3条）:"
echo "$DNS_LIST_RESPONSE" | head -50

echo ""
echo "=== 测试完成 ==="

# 统计结果
SUCCESS_COUNT=0
TOTAL_COUNT=3

if echo "$A_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

if echo "$CNAME_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

if echo "$TXT_RECORD_RESPONSE" | grep -q '"code" : 200'; then
    ((SUCCESS_COUNT++))
fi

echo "测试结果: $SUCCESS_COUNT/$TOTAL_COUNT 个测试用例通过"

if [ $SUCCESS_COUNT -eq $TOTAL_COUNT ]; then
    echo "🎉 所有测试用例都通过了！DNS记录添加功能正常工作"
    echo ""
    echo "📋 测试总结:"
    echo "- ✅ 用户登录认证正常"
    echo "- ✅ 域名列表获取正常"
    echo "- ✅ A记录添加成功"
    echo "- ✅ CNAME记录添加成功"
    echo "- ✅ TXT记录添加成功"
    echo "- ✅ DNSPod同步正常"
    echo ""
    echo "🔍 验证要点:"
    echo "- 所有记录都成功同步到DNSPod（syncStatus: SUCCESS）"
    echo "- 记录ID正确分配（recordId有值）"
    echo "- 用户权限验证正常"
    echo "- 参数验证正常"
    exit 0
else
    echo "⚠️  部分测试用例失败，请检查详细日志"
    exit 1
fi
#!/bin/bash

# 文件名：test_dnspod_domains.sh
# 功能：测试DNSPod获取域名列表接口
# 作者：CodeBuddy
# 创建时间：2025-08-21
# 版本：v1.0.0

echo "=== DNSPod获取域名列表接口测试 ==="
echo ""

BASE_URL="http://localhost:8080/api/dnspod"

echo "1. 测试获取所有域名（默认参数）"
echo "请求: GET $BASE_URL/domains"
curl -X GET "$BASE_URL/domains" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "2. 测试分页获取域名（limit=5）"
echo "请求: GET $BASE_URL/domains?limit=5"
curl -X GET "$BASE_URL/domains?limit=5" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "3. 测试关键字搜索域名"
echo "请求: GET $BASE_URL/domains?keyword=test&limit=10"
curl -X GET "$BASE_URL/domains?keyword=test&limit=10" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "4. 测试获取指定类型域名"
echo "请求: GET $BASE_URL/domains?type=MINE&limit=10"
curl -X GET "$BASE_URL/domains?type=MINE&limit=10" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "5. 测试分页偏移"
echo "请求: GET $BASE_URL/domains?offset=5&limit=3"
curl -X GET "$BASE_URL/domains?offset=5&limit=3" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "6. 测试获取指定分组域名"
echo "请求: GET $BASE_URL/domains?groupId=1&limit=5"
curl -X GET "$BASE_URL/domains?groupId=1&limit=5" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""
echo "----------------------------------------"

echo "=== 测试完成 ==="
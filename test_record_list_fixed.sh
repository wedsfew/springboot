#!/bin/bash

# 文件名：test_record_list_fixed.sh
# 功能：测试DNSPod获取解析记录列表接口（使用domainId）
# 作者：CodeBuddy
# 创建时间：2025-08-21
# 版本：v1.0.0

echo "=== DNSPod获取解析记录列表接口测试（使用domainId） ==="
echo "测试时间: $(date)"
echo ""

BASE_URL="http://localhost:8080/api/dnspod"

# 测试1: 使用domainId获取cblog.eu域名的所有解析记录
echo "1. 测试使用domainId获取cblog.eu域名的所有解析记录"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试2: 使用domainId获取cblog.eu域名的A记录
echo "2. 测试使用domainId获取cblog.eu域名的A记录"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&recordType=A"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&recordType=A" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试3: 使用domainId获取cblog.eu域名的tf子域名记录
echo "3. 测试使用domainId获取cblog.eu域名的tf子域名记录"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&subdomain=tf"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&subdomain=tf" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试4: 分页获取解析记录（限制2条）
echo "4. 测试分页获取解析记录（限制2条）"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&limit=2&offset=0"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&limit=2&offset=0" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试5: 按类型排序获取解析记录
echo "5. 测试按类型排序获取解析记录"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&sortField=type&sortType=ASC&limit=5"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&sortField=type&sortType=ASC&limit=5" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试6: 关键字搜索解析记录
echo "6. 测试关键字搜索解析记录"
echo "请求: GET ${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&keyword=tf"
curl -X GET "${BASE_URL}/records/list?domain=cblog.eu&domainId=98251198&keyword=tf" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试7: 测试vvvv.host域名（domainId=98261618）
echo "7. 测试vvvv.host域名的解析记录"
echo "请求: GET ${BASE_URL}/records/list?domain=vvvv.host&domainId=98261618&limit=3"
curl -X GET "${BASE_URL}/records/list?domain=vvvv.host&domainId=98261618&limit=3" \
  -H "Content-Type: application/json" \
  -w "\n状态码: %{http_code}\n响应时间: %{time_total}s\n" \
  | jq '.' 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

echo "=== 测试完成 ==="
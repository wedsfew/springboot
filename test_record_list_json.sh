#!/bin/bash

# DNSPod获取解析记录列表接口测试（JSON格式）
# 测试新的POST接口，使用application/json Content-Type

echo "=== DNSPod获取解析记录列表接口测试（JSON格式） ==="
echo "测试时间: $(date)"
echo ""

# 基础URL
BASE_URL="http://localhost:8080/api/dnspod/records/list"

# 测试1：获取cblog.eu域名的所有解析记录
echo "1. 测试获取cblog.eu域名的所有解析记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\"}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu"}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试2：获取cblog.eu域名的A记录
echo "2. 测试获取cblog.eu域名的A记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"recordType\":\"A\"}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","recordType":"A"}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试3：获取cblog.eu域名的sf子域名记录
echo "3. 测试获取cblog.eu域名的sf子域名记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"subdomain\":\"sf\"}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","subdomain":"sf"}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试4：分页获取解析记录（限制2条）
echo "4. 测试分页获取解析记录（限制2条，JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"limit\":2,\"offset\":0}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","limit":2,"offset":0}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试5：按类型排序获取解析记录
echo "5. 测试按类型排序获取解析记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"sortField\":\"type\",\"sortType\":\"ASC\",\"limit\":5}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","sortField":"type","sortType":"ASC","limit":5}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试6：关键字搜索解析记录
echo "6. 测试关键字搜索解析记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"keyword\":\"sf\"}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","keyword":"sf"}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试7：复合查询
echo "7. 测试复合查询（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"cblog.eu\",\"recordType\":\"A\",\"sortField\":\"name\",\"sortType\":\"DESC\",\"limit\":3}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"cblog.eu","recordType":"A","sortField":"name","sortType":"DESC","limit":3}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试8：测试vvvv.host域名的解析记录
echo "8. 测试vvvv.host域名的解析记录（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"vvvv.host\",\"limit\":3}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"vvvv.host","limit":3}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

# 测试9：错误情况 - 不存在的域名
echo "9. 测试错误情况 - 不存在的域名（JSON格式）"
echo "请求: POST $BASE_URL"
echo "请求体: {\"domain\":\"nonexistent.com\"}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"domain":"nonexistent.com"}' \
  2>/dev/null | python3 -m json.tool 2>/dev/null || echo "响应不是有效的JSON格式"
echo ""

echo "=== 测试完成 ==="
#!/bin/bash

BASE_URL="http://localhost:8080/api/domains"

echo "=== 域名同步功能接口测试 ==="

# 检查应用是否启动
echo "检查应用启动状态..."
if ! curl -s "$BASE_URL/count" > /dev/null 2>&1; then
    echo "❌ 应用未启动，请先启动Spring Boot应用"
    echo "启动命令: mvn spring-boot:run"
    exit 1
fi

echo "✅ 应用已启动，开始测试..."
echo ""

# 1. 测试域名同步
echo "1. 测试域名同步..."
echo "请求: POST $BASE_URL/sync"
response=$(curl -s -X POST "$BASE_URL/sync" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 2. 获取所有域名
echo "2. 获取所有域名..."
echo "请求: GET $BASE_URL"
response=$(curl -s -X GET "$BASE_URL" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 3. 统计域名总数
echo "3. 统计域名总数..."
echo "请求: GET $BASE_URL/count"
response=$(curl -s -X GET "$BASE_URL/count" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 4. 分页获取域名列表
echo "4. 分页获取域名列表..."
echo "请求: GET $BASE_URL/page?offset=0&limit=5"
response=$(curl -s -X GET "$BASE_URL/page?offset=0&limit=5" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 5. 搜索域名
echo "5. 搜索域名..."
echo "请求: GET $BASE_URL/search?keyword=cblog"
response=$(curl -s -X GET "$BASE_URL/search?keyword=cblog" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 6. 根据状态获取域名列表
echo "6. 根据状态获取域名列表..."
echo "请求: GET $BASE_URL/status/ENABLE"
response=$(curl -s -X GET "$BASE_URL/status/ENABLE" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 7. 根据状态统计域名数量
echo "7. 根据状态统计域名数量..."
echo "请求: GET $BASE_URL/count/status/ENABLE"
response=$(curl -s -X GET "$BASE_URL/count/status/ENABLE" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 8. 根据分组ID获取域名列表
echo "8. 根据分组ID获取域名列表..."
echo "请求: GET $BASE_URL/group/1"
response=$(curl -s -X GET "$BASE_URL/group/1" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 9. 测试保存域名信息
echo "9. 测试保存域名信息..."
echo "请求: POST $BASE_URL"
test_domain='{
  "domainId": 999999,
  "name": "test-domain.com",
  "punycode": "test-domain.com",
  "grade": "DP_Free",
  "gradeLevel": 1,
  "gradeTitle": "免费套餐",
  "isVip": "NO",
  "owner": "test@example.com",
  "status": "ENABLE",
  "groupId": 1,
  "searchEnginePush": "NO",
  "recordCount": 0,
  "ttl": 600,
  "cnameSpeedup": "DISABLE",
  "dnsStatus": "DNSPOD",
  "remark": "测试域名",
  "vipStartAt": null,
  "vipEndAt": null,
  "vipAutoRenew": "NO",
  "createdOn": "2025-08-21T08:00:00",
  "updatedOn": "2025-08-21T08:00:00"
}'
response=$(curl -s -X POST "$BASE_URL" -H "Content-Type: application/json" -d "$test_domain")
echo "响应: $response"
echo ""

# 10. 根据DNSPod域名ID获取域名
echo "10. 根据DNSPod域名ID获取域名..."
echo "请求: GET $BASE_URL/dnspod/999999"
response=$(curl -s -X GET "$BASE_URL/dnspod/999999" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 11. 根据域名名称获取域名
echo "11. 根据域名名称获取域名..."
echo "请求: GET $BASE_URL/name/test-domain.com"
response=$(curl -s -X GET "$BASE_URL/name/test-domain.com" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

# 12. 测试更新域名信息
echo "12. 测试更新域名信息..."
echo "请求: POST $BASE_URL/update"
updated_domain='{
  "domainId": 999999,
  "name": "test-domain.com",
  "punycode": "test-domain.com",
  "grade": "DP_Free",
  "gradeLevel": 1,
  "gradeTitle": "免费套餐",
  "isVip": "NO",
  "owner": "test@example.com",
  "status": "ENABLE",
  "groupId": 1,
  "searchEnginePush": "NO",
  "recordCount": 5,
  "ttl": 300,
  "cnameSpeedup": "DISABLE",
  "dnsStatus": "DNSPOD",
  "remark": "更新后的测试域名",
  "vipStartAt": null,
  "vipEndAt": null,
  "vipAutoRenew": "NO",
  "createdOn": "2025-08-21T08:00:00",
  "updatedOn": "2025-08-21T08:30:00"
}'
response=$(curl -s -X POST "$BASE_URL/update" -H "Content-Type: application/json" -d "$updated_domain")
echo "响应: $response"
echo ""

# 13. 测试删除域名信息
echo "13. 测试删除域名信息..."
echo "请求: POST $BASE_URL/delete/999999"
response=$(curl -s -X POST "$BASE_URL/delete/999999" -H "Content-Type: application/json")
echo "响应: $response"
echo ""

echo "=== 测试完成 ==="
echo ""
echo "测试总结："
echo "- 域名同步功能：从DNSPod获取域名列表并同步到本地数据库"
echo "- 域名查询功能：支持多种查询方式（ID、名称、状态、分组等）"
echo "- 域名管理功能：支持增删改查操作"
echo "- 统计功能：支持域名数量统计"
echo "- 搜索功能：支持关键字模糊搜索"
echo "- 分页功能：支持大量数据的分页查询"
echo ""
echo "注意事项："
echo "1. 确保DNSPod API配置正确（SecretId和SecretKey）"
echo "2. 确保数据库连接正常"
echo "3. 确保domain表已创建"
echo "4. 域名同步功能需要有效的DNSPod账户和域名"
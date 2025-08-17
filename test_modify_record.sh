#!/bin/bash

# DNSPod修改记录接口测试脚本
# 作者：CodeBuddy
# 创建时间：2025-08-17

# 设置基础URL
BASE_URL="http://localhost:8080"

# 设置输出颜色
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 创建测试报告目录
mkdir -p api测试报告

# 测试DNSPod修改记录接口
echo -e "${BLUE}测试: DNSPod修改记录接口${NC}"
echo -e "${YELLOW}测试修改域名解析记录功能${NC}"

# 使用URL编码确保参数安全
domain="cblog.eu"
recordId="2160417569"
recordType="A"
value="1.1.1.1"
recordLine="default"
subDomain="test"

echo -e "请求: GET $BASE_URL/api/test/dnspod/modify-record?domain=$domain&recordId=$recordId&recordType=$recordType&value=$value&recordLine=$recordLine&subDomain=$subDomain"

response=$(curl -s "$BASE_URL/api/test/dnspod/modify-record?domain=$domain&recordId=$recordId&recordType=$recordType&value=$value&recordLine=$recordLine&subDomain=$subDomain")

# 检查响应是否为有效的JSON
if ! echo "$response" | jq . > /dev/null 2>&1; then
    echo -e "${RED}错误: 响应不是有效的JSON格式${NC}"
    echo "原始响应:"
    echo "$response"
    
    # 将测试结果写入报告
    report_file="api测试报告/DNSPod修改记录接口_测试报告.md"
    echo "# DNSPod修改记录接口测试报告" > $report_file
    echo "## 测试信息" >> $report_file
    echo "- **测试时间**: $(date '+%Y-%m-%d %H:%M:%S')" >> $report_file
    echo "- **测试接口**: GET $BASE_URL/api/test/dnspod/modify-record" >> $report_file
    echo "- **测试描述**: 测试修改域名解析记录功能" >> $report_file
    echo "" >> $report_file
    
    echo "## 请求参数" >> $report_file
    echo "- domain: $domain" >> $report_file
    echo "- recordId: $recordId" >> $report_file
    echo "- recordType: $recordType" >> $report_file
    echo "- value: $value" >> $report_file
    echo "- recordLine: $recordLine" >> $report_file
    echo "- subDomain: $subDomain" >> $report_file
    echo "" >> $report_file
    
    echo "## 响应结果" >> $report_file
    echo '```' >> $report_file
    echo "$response" >> $report_file
    echo '```' >> $report_file
    echo "" >> $report_file
    
    echo "## 测试结果: ❌ 失败" >> $report_file
    echo "- 响应不是有效的JSON格式" >> $report_file
    
    echo "## 结论" >> $report_file
    echo "DNSPod修改记录接口测试失败，响应不是有效的JSON格式。请检查服务器日志以获取更多信息。" >> $report_file
    
    echo -e "${BLUE}测试完成，测试报告已保存到 $report_file${NC}"
    exit 1
fi

# 提取响应码
code=$(echo $response | jq -r '.code')

# 检查响应码是否符合预期
if [ "$code" == "200" ]; then
    echo -e "${GREEN}测试通过! 响应码: $code${NC}"
else
    echo -e "${RED}测试失败! 预期响应码: 200, 实际响应码: $code${NC}"
fi

# 打印响应
echo "响应:"
echo $response | jq .
echo ""

# 将测试结果写入报告
report_file="api测试报告/DNSPod修改记录接口_测试报告.md"
echo "# DNSPod修改记录接口测试报告" > $report_file
echo "## 测试信息" >> $report_file
echo "- **测试时间**: $(date '+%Y-%m-%d %H:%M:%S')" >> $report_file
echo "- **测试接口**: GET $BASE_URL/api/test/dnspod/modify-record" >> $report_file
echo "- **测试描述**: 测试修改域名解析记录功能" >> $report_file
echo "" >> $report_file

echo "## 请求参数" >> $report_file
echo "- domain: $domain" >> $report_file
echo "- recordId: $recordId" >> $report_file
echo "- recordType: $recordType" >> $report_file
echo "- value: $value" >> $report_file
echo "- recordLine: $recordLine" >> $report_file
echo "- subDomain: $subDomain" >> $report_file
echo "" >> $report_file

echo "## 响应结果" >> $report_file
echo '```json' >> $report_file
echo $response | jq . >> $report_file
echo '```' >> $report_file
echo "" >> $report_file

if [ "$code" == "200" ]; then
    echo "## 测试结果: ✅ 通过" >> $report_file
else
    echo "## 测试结果: ❌ 失败" >> $report_file
    echo "- 预期响应码: 200" >> $report_file
    echo "- 实际响应码: $code" >> $report_file
fi

echo "## 结论" >> $report_file
if [ "$code" == "200" ]; then
    echo "DNSPod修改记录接口功能正常，能够成功修改域名解析记录。" >> $report_file
else
    echo "DNSPod修改记录接口测试失败，请检查错误信息并修复问题。" >> $report_file
    error_message=$(echo $response | jq -r '.message')
    echo "错误信息: $error_message" >> $report_file
fi

echo -e "${BLUE}测试完成，测试报告已保存到 $report_file${NC}"
# DNSPod删除记录接口文档

## 接口概述
提供腾讯云DNSPod域名解析记录删除功能，支持删除指定域名的解析记录。

## 基础信息
- **Base URL**: `http://localhost:8080/api/dnspod`
- **Content-Type**: `application/json`
- **认证方式**: JWT Token

## API接口详情

### 1. 删除域名解析记录（完整版）
- **请求路径**: `DELETE /api/dnspod/record`
- **请求参数**:
```json
{
  "domain": "dnspod.cn",
  "recordId": 162,
  "domainId": 1923
}
```

### 2. 删除域名解析记录（简化版）
- **请求路径**: `DELETE /api/dnspod/record/{domain}/{recordId}`
- **路径参数**: domain（域名）, recordId（记录ID）

## 响应示例

### 成功响应
```json
{
  "code": 200,
  "message": "删除域名解析记录成功",
  "data": {
    "RequestId": "ab4f1426-ea15-42ea-8183-dc1b44151166"
  },
  "timestamp": "2025-01-16T10:30:00+08:00"
}
```

### 失败响应
```json
{
  "code": 500,
  "message": "删除域名解析记录失败: 记录编号错误",
  "data": null,
  "timestamp": "2025-01-16T10:31:00+08:00"
}
```

## 测试方法

### curl测试
```bash
# 完整版接口
curl -X DELETE -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"domain":"cblog.eu","recordId":2167176579}' \
  http://localhost:8080/api/dnspod/record

# 简化版接口
curl -X DELETE -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/dnspod/record/cblog.eu/2167176579
```

## 注意事项
1. 删除操作不可逆，请谨慎操作
2. 需要先通过查询接口获取正确的记录ID
3. 只能删除当前腾讯云账户下的域名解析记录
4. API有频率限制，请合理控制调用频率
# DNSPod修改记录接口文档

## 接口概述

本接口用于修改腾讯云DNSPod的域名解析记录。

## 接口信息

- **接口名称**: 修改域名解析记录
- **请求方法**: PUT
- **接口路径**: `/api/dnspod/record`
- **Content-Type**: `application/json`

## 请求参数

### 请求体 (JSON)

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| domain | String | 是 | 域名 | "example.com" |
| recordType | String | 是 | 记录类型，如A、CNAME、MX等 | "A" |
| recordLine | String | 是 | 记录线路，默认为"默认" | "默认" |
| value | String | 是 | 记录值 | "192.168.1.1" |
| recordId | Long | 是 | 记录ID通过DNSPod域名解析记录接口获取 | 123456789 |
| domainId | Long | 否 | 域名ID，优先级比domain高 | 123456 |
| subDomain | String | 否 | 子域名 | "www" |
| recordLineId | String | 否 | 记录线路ID | "0" |
| mx | Integer | 否 | MX优先级，仅MX记录需要 | 10 |
| ttl | Integer | 否 | TTL值，范围1-604800 | 600 |
| weight | Integer | 否 | 权重值，范围0-100 | 10 |
| status | String | 否 | 记录状态，"ENABLE"或"DISABLE" | "ENABLE" |
| remark | String | 否 | 记录备注 | "测试记录" |
| dnssecConflictMode | String | 否 | DNSSEC冲突模式 | "IGNORE" |

### 请求示例

```json
{
  "domain": "example.com",
  "recordType": "A",
  "recordLine": "默认",
  "value": "192.168.1.100",
  "recordId": 123456789,
  "subDomain": "www",
  "ttl": 600,
  "remark": "修改后的记录"
}
```

## 响应参数

### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "RecordId": 123456789,
    "RequestId": "12345678-1234-1234-1234-123456789012"
  },
  "timestamp": "2025-08-17T10:30:00.123456789"
}
```

### 错误响应

```json
{
  "code": 500,
  "message": "修改记录失败: 记录编号错误。",
  "data": null,
  "timestamp": "2025-08-17T10:30:00.123456789"
}
```

## 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 响应状态码，200表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据 |
| data.RecordId | Long | 记录ID |
| data.RequestId | String | 请求ID |
| timestamp | String | 响应时间戳 |

## 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 500 | 服务器内部错误 |

## 常见错误

| 错误信息 | 原因 | 解决方案 |
|----------|------|----------|
| 域名编号不正确 | 域名不存在或无权限 | 检查域名是否正确，确认域名已添加到DNSPod |
| 记录编号错误 | 记录ID不存在 | 检查记录ID是否正确 |
| 记录类型错误 | 不支持的记录类型 | 使用支持的记录类型（A、CNAME、MX等） |
| 记录值格式错误 | 记录值格式不正确 | 根据记录类型提供正确格式的值 |

## 测试接口

为了方便测试，系统提供了以下测试接口：

### 1. 完整参数测试
- **路径**: `/api/test/dnspod/modify-record`
- **方法**: POST
- **说明**: 测试修改记录接口的完整功能

### 2. 简化参数测试
- **路径**: `/api/test/dnspod/modify-record-simple`
- **方法**: POST
- **说明**: 测试修改记录接口的基本功能

### 3. 错误参数测试
- **路径**: `/api/test/dnspod/modify-record-error`
- **方法**: POST
- **说明**: 测试错误处理机制

## 使用示例

### cURL 示例

```bash
# 修改A记录
curl -X PUT "http://localhost:8081/api/dnspod/record" \
  -H "Content-Type: application/json" \
  -d '{
    "domain": "example.com",
    "recordType": "A",
    "recordLine": "默认",
    "value": "192.168.1.100",
    "recordId": 123456789,
    "subDomain": "www",
    "ttl": 600,
    "remark": "修改后的记录"
  }'
```

### JavaScript 示例

```javascript
const modifyRecord = async () => {
  const response = await fetch('http://localhost:8081/api/dnspod/record', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      domain: 'example.com',
      recordType: 'A',
      recordLine: '默认',
      value: '192.168.1.100',
      recordId: 123456789,
      subDomain: 'www',
      ttl: 600,
      remark: '修改后的记录'
    })
  });
  
  const result = await response.json();
  console.log(result);
};
```

## 注意事项

1. **权限要求**: 需要有效的腾讯云API密钥
2. **记录ID**: 必须是有效的记录ID，可通过查询接口获取
3. **域名验证**: 域名必须已添加到DNSPod并有管理权限
4. **记录类型**: 不同记录类型对应不同的值格式要求
5. **TTL限制**: TTL值范围为1-604800秒
6. **线路选择**: 记录线路需要根据实际需求选择

## 版本信息

- **版本**: v1.0.0
- **创建时间**: 2025-08-17
- **最后更新**: 2025-08-17
- **作者**: CodeBuddy
# DNSPod获取解析记录列表接口文档

## 接口概述

本接口基于腾讯云DNSPod API的DescribeRecordList功能，用于获取指定域名下的解析记录列表。支持多种查询条件和排序方式。

## 接口信息

- **接口标识**: `DNSPOD_GET_RECORD_LIST`
- **请求路径**: `GET /api/dnspod/records/list`
- **接口描述**: 获取域名的解析记录列表，支持分页、排序、筛选等功能
- **认证要求**: 无需认证（当前配置）
- **适用业务单元**: DNS解析记录管理

## 请求参数

### 必填参数

| 参数名 | 类型 | 描述 | 示例值 |
|--------|------|------|--------|
| domain | String | 域名名称 | cblog.eu |

### 可选参数

| 参数名 | 类型 | 描述 | 示例值 |
|--------|------|------|--------|
| domainId | Integer | 域名ID（优先级比domain高） | 98251198 |
| subdomain | String | 解析记录的主机头 | www |
| recordType | String | 记录类型（A、CNAME、NS、AAAA等） | A |
| recordLine | String | 线路名称 | 默认 |
| recordLineId | String | 线路ID（优先级比recordLine高） | 0 |
| groupId | Integer | 分组ID | 1 |
| keyword | String | 关键字搜索（支持搜索主机头和记录值） | test |
| sortField | String | 排序字段（name,line,type,value,weight,mx,ttl,updated_on） | type |
| sortType | String | 排序方式（ASC或DESC，默认ASC） | ASC |
| offset | Integer | 偏移量（默认0） | 0 |
| limit | Integer | 限制数量（默认100，最大3000） | 10 |

## 响应格式

### 成功响应（200）

```json
{
  "code": 200,
  "message": "获取解析记录列表成功",
  "data": {
    "skipSign": false,
    "requestId": "xxx-xxx-xxx",
    "recordCountInfo": {
      "skipSign": false,
      "subdomainCount": 2,
      "totalCount": 2,
      "listCount": 2
    },
    "recordList": [
      {
        "recordId": 556507778,
        "value": "f1g1ns1.dnspod.net.",
        "status": "ENABLE",
        "updatedOn": "2021-03-28 11:27:09",
        "name": "@",
        "line": "默认",
        "lineId": "0",
        "type": "NS",
        "weight": null,
        "monitorStatus": "",
        "remark": "",
        "ttl": 86400,
        "mx": 0,
        "defaultNS": true
      }
    ]
  },
  "timestamp": "2025-08-21T08:55:00Z"
}
```

### 失败响应

#### 域名格式错误（500）

```json
{
  "code": 500,
  "message": "获取解析记录列表失败: 调用腾讯云DNSPod API失败: 域名不正确，请输入主域名，如 dnspod.cn。",
  "data": null,
  "timestamp": "2025-08-21T08:55:00Z"
}
```

#### 权限不足（500）

```json
{
  "code": 500,
  "message": "获取解析记录列表失败: 调用腾讯云DNSPod API失败: 域名不在您的名下。",
  "data": null,
  "timestamp": "2025-08-21T08:55:00Z"
}
```

## 使用示例

### 示例1：获取域名的所有解析记录

```bash
curl -X GET "http://localhost:8080/api/dnspod/records/list?domain=cblog.eu" \
  -H "Content-Type: application/json"
```

### 示例2：获取A记录

```bash
curl -X GET "http://localhost:8080/api/dnspod/records/list?domain=cblog.eu&recordType=A" \
  -H "Content-Type: application/json"
```

### 示例3：分页查询

```bash
curl -X GET "http://localhost:8080/api/dnspod/records/list?domain=cblog.eu&limit=10&offset=0" \
  -H "Content-Type: application/json"
```

### 示例4：关键字搜索

```bash
curl -X GET "http://localhost:8080/api/dnspod/records/list?domain=cblog.eu&keyword=www" \
  -H "Content-Type: application/json"
```

### 示例5：按类型排序

```bash
curl -X GET "http://localhost:8080/api/dnspod/records/list?domain=cblog.eu&sortField=type&sortType=ASC" \
  -H "Content-Type: application/json"
```

## 注意事项

1. **域名格式要求**: 腾讯云DNSPod API对域名格式有严格要求，必须是有效的主域名格式
2. **权限验证**: 只能查询当前账户下的域名解析记录
3. **API限制**: 默认接口请求频率限制为100次/秒
4. **数据延迟**: 新添加的解析记录存在短暂的索引延迟，如果查询不到新增记录，请在30秒后重试
5. **记录数量**: API获取的记录总条数会比控制台多2条NS记录

## 错误码说明

| 错误码 | 描述 | 解决方案 |
|--------|------|----------|
| InvalidParameter.DomainInvalid | 域名不正确 | 请输入有效的主域名格式 |
| FailedOperation.NotDomainOwner | 域名不在您的名下 | 请确认域名所有权 |
| InvalidParameterValue.LimitInvalid | 分页长度数量错误 | 请检查limit参数范围 |
| InvalidParameter.ResultMoreThan500 | 搜索结果大于500条 | 请增加关键字缩小搜索范围 |
| RequestLimitExceeded | 请求次数超过频率限制 | 请降低请求频率 |

## 相关接口

- [DNSPod获取域名列表接口](./DNSPod获取域名列表接口.md)
- [DNSPod添加记录接口](./DNSPod添加记录接口.md)
- [DNSPod修改记录接口](./DNSPod修改记录接口.md)
- [DNSPod删除记录接口](./DNSPod删除记录接口.md)

## 版本历史

| 版本 | 日期 | 变更内容 |
|------|------|----------|
| v1.0.0 | 2025-08-21 | 初始版本，实现基础的解析记录列表查询功能 |
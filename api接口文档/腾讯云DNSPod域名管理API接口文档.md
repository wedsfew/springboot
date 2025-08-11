# 腾讯云DNSPod域名管理API接口文档

## 一、接口概述

### 1.1 功能描述
本模块提供腾讯云DNSPod域名管理功能，包括获取域名列表、同步域名数据、域名信息查询等核心功能。

### 1.2 基础信息
- **Base URL**: `http://localhost:8080/api/v1`
- **认证方式**: JWT Bearer Token
- **Content-Type**: `application/json`
- **字符编码**: `UTF-8`

## 二、API接口详情

### 2.1 获取本地域名列表

#### 基本信息
- **接口标识**: `DOMAIN_GET_ALL`
- **请求路径**: `GET /domains`
- **接口描述**: 获取本地数据库中存储的所有域名信息
- **认证要求**: 需要JWT认证

#### 请求示例
```bash
curl -X GET "http://localhost:8080/api/v1/domains" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

#### 响应示例
```json
{
  "code": 200,
  "message": "获取域名列表成功",
  "data": [
    {
      "id": 1,
      "domainId": 98251198,
      "domainName": "cblog.eu",
      "status": "ENABLE",
      "grade": "DP_FREE",
      "gradeTitle": "免费版",
      "isMark": false,
      "ttl": 600,
      "dnsStatus": "",
      "minTtl": 600,
      "recordCount": 0,
      "createdOn": "2025-08-08T05:00:24",
      "updatedOn": "2025-08-09T00:02:21",
      "owner": "qcloud_uin_100031728875@qcloud.com",
      "remark": "",
      "createTime": "2025-08-11T13:26:49",
      "updateTime": "2025-08-11T13:26:49"
    }
  ],
  "timestamp": "2025-08-11T13:27:00.125137"
}
```

### 2.2 从腾讯云获取域名列表

#### 基本信息
- **接口标识**: `DOMAIN_GET_FROM_CLOUD`
- **请求路径**: `GET /domains/cloud`
- **接口描述**: 直接从腾讯云DNSPod获取域名列表（不保存到数据库）
- **认证要求**: 需要JWT认证

#### 请求示例
```bash
curl -X GET "http://localhost:8080/api/v1/domains/cloud" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

#### 响应示例
```json
{
  "code": 200,
  "message": "从腾讯云获取域名列表成功",
  "data": [
    {
      "id": null,
      "domainId": 98251198,
      "domainName": "cblog.eu",
      "status": "ENABLE",
      "grade": "DP_FREE",
      "gradeTitle": "免费版",
      "isMark": false,
      "ttl": 600,
      "dnsStatus": "",
      "minTtl": 600,
      "recordCount": 0,
      "createdOn": "2025-08-08T05:00:24",
      "updatedOn": "2025-08-09T00:02:21",
      "owner": "qcloud_uin_100031728875@qcloud.com",
      "remark": "",
      "createTime": null,
      "updateTime": null
    }
  ],
  "timestamp": "2025-08-11T13:26:37.61244"
}
```

### 2.3 同步域名数据

#### 基本信息
- **接口标识**: `DOMAIN_SYNC`
- **请求路径**: `POST /domains/sync`
- **接口描述**: 从腾讯云DNSPod获取最新域名列表并同步到本地数据库
- **认证要求**: 需要JWT认证

#### 请求示例
```bash
curl -X POST "http://localhost:8080/api/v1/domains/sync" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

#### 响应示例
```json
{
  "code": 200,
  "message": "域名数据同步成功",
  "data": "域名同步完成，新增: 2个，更新: 0个，总计: 2个",
  "timestamp": "2025-08-11T13:26:49.554482"
}
```

### 2.4 根据域名ID获取域名信息

#### 基本信息
- **接口标识**: `DOMAIN_GET_BY_ID`
- **请求路径**: `GET /domains/{domainId}`
- **接口描述**: 根据腾讯云域名ID获取域名详细信息
- **认证要求**: 需要JWT认证

#### 请求参数
| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| domainId | Long | Path | 是 | 腾讯云域名ID |

#### 请求示例
```bash
curl -X GET "http://localhost:8080/api/v1/domains/98251198" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

#### 响应示例
```json
{
  "code": 200,
  "message": "获取域名信息成功",
  "data": {
    "id": 1,
    "domainId": 98251198,
    "domainName": "cblog.eu",
    "status": "ENABLE",
    "grade": "DP_FREE",
    "gradeTitle": "免费版",
    "isMark": false,
    "ttl": 600,
    "dnsStatus": "",
    "minTtl": 600,
    "recordCount": 0,
    "createdOn": "2025-08-08T05:00:24",
    "updatedOn": "2025-08-09T00:02:21",
    "owner": "qcloud_uin_100031728875@qcloud.com",
    "remark": "",
    "createTime": "2025-08-11T13:26:49",
    "updateTime": "2025-08-11T13:26:49"
  },
  "timestamp": "2025-08-11T13:27:00.125137"
}
```

### 2.5 根据域名名称获取域名信息

#### 基本信息
- **接口标识**: `DOMAIN_GET_BY_NAME`
- **请求路径**: `GET /domains/name/{domainName}`
- **接口描述**: 根据域名名称获取域名详细信息
- **认证要求**: 需要JWT认证

#### 请求参数
| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| domainName | String | Path | 是 | 域名名称（如：cblog.eu） |

#### 请求示例
```bash
curl -X GET "http://localhost:8080/api/v1/domains/name/cblog.eu" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

### 2.6 删除域名信息

#### 基本信息
- **接口标识**: `DOMAIN_DELETE`
- **请求路径**: `DELETE /domains/{domainId}`
- **接口描述**: 删除本地数据库中的域名信息（不影响腾讯云）
- **认证要求**: 需要JWT认证

#### 请求参数
| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| domainId | Long | Path | 是 | 腾讯云域名ID |

#### 请求示例
```bash
curl -X DELETE "http://localhost:8080/api/v1/domains/98251198" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

#### 响应示例
```json
{
  "code": 200,
  "message": "域名删除成功",
  "data": "删除成功",
  "timestamp": "2025-08-11T13:30:00.125137"
}
```

## 三、数据模型

### 3.1 Domain实体结构
```json
{
  "id": "Long - 本地数据库主键ID",
  "domainId": "Long - 腾讯云域名ID",
  "domainName": "String - 域名名称",
  "status": "String - 域名状态（ENABLE/DISABLE）",
  "grade": "String - 域名套餐等级",
  "gradeTitle": "String - 套餐等级中文名称",
  "isMark": "Boolean - 是否标记",
  "ttl": "Integer - 默认TTL值",
  "dnsStatus": "String - DNS状态",
  "minTtl": "Integer - 最小TTL值",
  "recordCount": "Integer - 记录数量",
  "createdOn": "LocalDateTime - 腾讯云创建时间",
  "updatedOn": "LocalDateTime - 腾讯云更新时间",
  "owner": "String - 域名所有者",
  "remark": "String - 备注信息",
  "createTime": "LocalDateTime - 本地创建时间",
  "updateTime": "LocalDateTime - 本地更新时间"
}
```

## 四、错误码说明

| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200 | 成功 | 请求处理成功 |
| 401 | 未授权 | JWT token无效或过期 |
| 404 | 资源不存在 | 域名不存在 |
| 500 | 服务器错误 | 腾讯云API调用失败或数据库错误 |

## 五、使用说明

### 5.1 认证流程
1. 使用管理员账号登录获取JWT token
2. 在请求头中添加 `Authorization: Bearer {token}`
3. Token有效期24小时

### 5.2 典型使用场景
1. **首次使用**：调用同步接口将腾讯云域名数据导入本地
2. **日常查询**：使用本地接口快速获取域名列表
3. **数据更新**：定期调用同步接口保持数据最新
4. **实时查询**：使用云端接口获取最新状态

### 5.3 注意事项
- 腾讯云API有调用频率限制，建议合理使用同步功能
- 删除操作仅删除本地数据，不影响腾讯云域名
- 建议定期备份域名数据
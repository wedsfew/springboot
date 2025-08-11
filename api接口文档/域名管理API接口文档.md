# 域名管理API接口文档

## 一、接口概述

### 1.1 功能描述
域名管理API提供腾讯云DNSPod域名的查询、同步和管理功能，支持从腾讯云获取域名列表并同步到本地数据库。

### 1.2 基础信息
- **Base URL**: `http://localhost:8080/api/v1`
- **Content-Type**: `application/json`
- **字符编码**: `UTF-8`
- **认证要求**: 需要JWT认证（除特殊说明外）

## 二、API接口列表

### 2.1 获取所有域名信息

#### 基本信息
- **接口标识**: `DOMAIN_GET_ALL`
- **请求路径**: `GET /domains`
- **接口描述**: 获取本地数据库中存储的所有域名信息
- **认证要求**: 需要JWT认证

#### 请求参数
无

#### 响应示例

##### 成功响应（200）
```json
{
  "code": 200,
  "message": "获取域名列表成功",
  "data": [
    {
      "id": 1,
      "domainId": 123456,
      "domainName": "cblog.eu",
      "status": "ACTIVE",
      "grade": "D_Free",
      "gradeTitle": "免费版",
      "isMark": false,
      "ttl": 600,
      "dnsStatus": "DNSPOD",
      "minTtl": 1,
      "recordCount": 5,
      "createdOn": "2023-01-01T12:00:00",
      "updatedOn": "2023-01-01T12:00:00",
      "owner": "user@example.com",
      "remark": "主域名",
      "createTime": "2025-01-11T13:00:00",
      "updateTime": "2025-01-11T13:00:00"
    }
  ],
  "timestamp": "2025-01-11T13:00:00Z"
}
```

##### 失败响应（500）
```json
{
  "code": 500,
  "message": "获取域名列表失败: 数据库连接异常",
  "data": null,
  "timestamp": "2025-01-11T13:00:00Z"
}
```

### 2.2 根据域名ID获取域名信息

#### 基本信息
- **接口标识**: `DOMAIN_GET_BY_ID`
- **请求路径**: `GET /domains/{domainId}`
- **接口描述**: 根据腾讯云域名ID获取域名详细信息
- **认证要求**: 需要JWT认证

#### 请求参数
- **domainId** (路径参数): 腾讯云域名ID，必填，数字类型

#### 响应示例

##### 成功响应（200）
```json
{
  "code": 200,
  "message": "获取域名信息成功",
  "data": {
    "id": 1,
    "domainId": 123456,
    "domainName": "cblog.eu",
    "status": "ACTIVE",
    "grade": "D_Free",
    "gradeTitle": "免费版",
    "isMark": false,
    "ttl": 600,
    "dnsStatus": "DNSPOD",
    "minTtl": 1,
    "recordCount": 5,
    "createdOn": "2023-01-01T12:00:00",
    "updatedOn": "2023-01-01T12:00:00",
    "owner": "user@example.com",
    "remark": "主域名",
    "createTime": "2025-01-11T13:00:00",
    "updateTime": "2025-01-11T13:00:00"
  },
  "timestamp": "2025-01-11T13:00:00Z"
}
```

##### 失败响应（404）
```json
{
  "code": 404,
  "message": "域名不存在",
  "data": null,
  "timestamp": "2025-01-11T13:00:00Z"
}
```

### 2.3 根据域名名称获取域名信息

#### 基本信息
- **接口标识**: `DOMAIN_GET_BY_NAME`
- **请求路径**: `GET /domains/name/{domainName}`
- **接口描述**: 根据域名名称获取域名详细信息
- **认证要求**: 需要JWT认证

#### 请求参数
- **domainName** (路径参数): 域名名称，必填，字符串类型

#### 响应示例
参考2.2节的响应格式

### 2.4 从腾讯云获取域名列表

#### 基本信息
- **接口标识**: `DOMAIN_GET_FROM_CLOUD`
- **请求路径**: `GET /domains/cloud`
- **接口描述**: 直接从腾讯云DNSPod获取域名列表（不保存到数据库）
- **认证要求**: 需要JWT认证

#### 请求参数
无

#### 响应示例

##### 成功响应（200）
```json
{
  "code": 200,
  "message": "从腾讯云获取域名列表成功",
  "data": [
    {
      "domainId": 123456,
      "domainName": "cblog.eu",
      "status": "ACTIVE",
      "grade": "D_Free",
      "gradeTitle": "免费版",
      "isMark": false,
      "ttl": 600,
      "dnsStatus": "DNSPOD",
      "minTtl": 1,
      "recordCount": 5,
      "createdOn": "2023-01-01T12:00:00",
      "updatedOn": "2023-01-01T12:00:00",
      "owner": "user@example.com",
      "remark": "主域名"
    }
  ],
  "timestamp": "2025-01-11T13:00:00Z"
}
```

##### 失败响应（500）
```json
{
  "code": 500,
  "message": "从腾讯云获取域名列表失败: API调用异常",
  "data": null,
  "timestamp": "2025-01-11T13:00:00Z"
}
```

### 2.5 同步域名数据

#### 基本信息
- **接口标识**: `DOMAIN_SYNC`
- **请求路径**: `POST /domains/sync`
- **接口描述**: 从腾讯云DNSPod获取最新域名列表并同步到本地数据库
- **认证要求**: 需要JWT认证

#### 请求参数
无

#### 响应示例

##### 成功响应（200）
```json
{
  "code": 200,
  "message": "域名数据同步成功",
  "data": "域名同步完成，新增: 1个，更新: 0个，总计: 1个",
  "timestamp": "2025-01-11T13:00:00Z"
}
```

##### 失败响应（500）
```json
{
  "code": 500,
  "message": "域名数据同步失败: 腾讯云API调用失败",
  "data": null,
  "timestamp": "2025-01-11T13:00:00Z"
}
```

### 2.6 删除域名信息

#### 基本信息
- **接口标识**: `DOMAIN_DELETE`
- **请求路径**: `DELETE /domains/{domainId}`
- **接口描述**: 从本地数据库删除指定域名信息（不影响腾讯云）
- **认证要求**: 需要JWT认证

#### 请求参数
- **domainId** (路径参数): 腾讯云域名ID，必填，数字类型

#### 响应示例

##### 成功响应（200）
```json
{
  "code": 200,
  "message": "域名删除成功",
  "data": "删除成功",
  "timestamp": "2025-01-11T13:00:00Z"
}
```

##### 失败响应（404）
```json
{
  "code": 404,
  "message": "域名不存在或删除失败",
  "data": null,
  "timestamp": "2025-01-11T13:00:00Z"
}
```

## 三、数据模型

### 3.1 Domain实体结构
```typescript
interface Domain {
  id: number;              // 主键ID
  domainId: number;        // 腾讯云域名ID
  domainName: string;      // 域名名称
  status: string;          // 域名状态：ACTIVE-正常，PAUSE-暂停，SPAM-封禁
  grade: string;           // 域名等级：D_Free-免费版，D_Plus-个人豪华版等
  gradeTitle: string;      // 域名等级标题
  isMark: boolean;         // 是否星标
  ttl: number;             // TTL值
  dnsStatus: string;       // DNS状态：DNSPOD-使用DNSPod，QCLOUD-使用腾讯云解析
  minTtl: number;          // 最小TTL值
  recordCount: number;     // 记录数量
  createdOn: string;       // 域名创建时间（腾讯云）
  updatedOn: string;       // 域名更新时间（腾讯云）
  owner: string;           // 域名所有者
  remark: string;          // 备注信息
  createTime: string;      // 记录创建时间
  updateTime: string;      // 记录更新时间
}
```

## 四、错误码说明

| 错误码 | 含义 | 处理建议 |
|--------|------|----------|
| 200 | 成功 | 正常处理返回数据 |
| 400 | 请求参数错误 | 检查请求参数格式和必填项 |
| 401 | 未授权 | 检查JWT token是否有效 |
| 404 | 资源不存在 | 确认域名ID或名称是否正确 |
| 500 | 服务器内部错误 | 检查服务器日志，可能是数据库或API调用异常 |

## 五、使用示例

### 5.1 获取域名列表
```bash
curl -X GET "http://localhost:8080/api/v1/domains" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 5.2 同步域名数据
```bash
curl -X POST "http://localhost:8080/api/v1/domains/sync" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 5.3 从腾讯云获取域名列表
```bash
curl -X GET "http://localhost:8080/api/v1/domains/cloud" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

## 六、注意事项

1. **认证要求**: 所有接口都需要有效的JWT token
2. **腾讯云配置**: 确保腾讯云SecretId和SecretKey配置正确
3. **数据库表**: 使用前需要先创建domain_info表
4. **同步频率**: 建议不要频繁调用同步接口，避免超出腾讯云API调用限制
5. **错误处理**: 调用方应该根据返回的错误码进行相应的错误处理
6. **数据一致性**: 删除本地域名数据不会影响腾讯云上的域名配置
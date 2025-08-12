# DNSPod API接口文档

## 基本信息

- **基础路径**: `/api/dnspod`
- **认证要求**: 需要JWT认证
- **数据格式**: JSON

## 域名管理接口

### 获取域名列表

- **接口路径**: `GET /domains`
- **描述**: 获取用户的所有域名列表
- **请求参数**: 无
- **响应示例**:

```json
{
  "code": 200,
  "message": "获取域名列表成功",
  "data": [
    {
      "domainId": 12345,
      "name": "example.com",
      "status": "ENABLE",
      "recordCount": 5
    }
  ],
  "timestamp": "2025-08-12T10:30:00Z"
}
```

## 记录管理接口

### 获取记录列表

- **接口路径**: `GET /domains/{domain}/records`
- **描述**: 获取指定域名下的所有记录
- **请求参数**:
  - `domain`: 域名，如 example.com
- **响应示例**:

```json
{
  "code": 200,
  "message": "获取记录列表成功",
  "data": [
    {
      "recordId": 12345,
      "subDomain": "www",
      "recordType": "A",
      "recordLine": "默认",
      "value": "192.168.1.1",
      "ttl": 600,
      "status": "ENABLE"
    }
  ],
  "timestamp": "2025-08-12T10:30:00Z"
}
```

### 获取记录列表（高级查询）

- **接口路径**: `GET /domains/{domain}/records/search`
- **描述**: 获取指定域名下的解析记录，支持多种查询条件
- **请求参数**:
  - `domain`: 域名，如 example.com
  - `domainId`: (可选) 域名ID，如果提供则优先使用
  - `subdomain`: (可选) 主机头，如果提供则只返回此主机头对应的记录
  - `recordType`: (可选) 记录类型，如A，CNAME，NS等
  - `recordLine`: (可选) 线路名称
  - `recordLineId`: (可选) 线路ID，优先于recordLine
  - `groupId`: (可选) 分组ID
  - `keyword`: (可选) 关键字，用于搜索主机头和记录值
  - `sortField`: (可选) 排序字段，支持name,line,type,value,weight,mx,ttl,updated_on
  - `sortType`: (可选) 排序方式，ASC(正序)或DESC(逆序)，默认ASC
  - `offset`: (可选) 偏移量，默认0
  - `limit`: (可选) 限制数量，默认100，最大3000
- **响应示例**:

```json
{
  "code": 200,
  "message": "获取记录列表成功",
  "data": {
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
    ],
    "recordCountInfo": {
      "subdomainCount": 2,
      "totalCount": 2,
      "listCount": 2
    },
    "success": true,
    "message": "获取记录列表成功",
    "requestId": "561cdfcb-37a6-47de-b3c5-2b038e2c2276"
  },
  "timestamp": "2025-08-12T10:30:00Z"
}
```

### 创建记录

- **接口路径**: `POST /domains/{domain}/records`
- **描述**: 在指定域名下创建新的记录
- **请求参数**:
  - `domain`: 域名，如 example.com
  - 请求体:
    ```json
    {
      "subDomain": "www",
      "recordType": "A",
      "recordLine": "默认",
      "value": "192.168.1.1",
      "ttl": 600
    }
    ```
- **响应示例**:

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "recordId": 12345,
    "success": true,
    "message": "创建记录成功"
  },
  "timestamp": "2025-08-12T10:30:00Z"
}
```

### 修改记录

- **接口路径**: `PUT /domains/{domain}/records/{recordId}`
- **描述**: 修改指定域名下的记录
- **请求参数**:
  - `domain`: 域名，如 example.com
  - `recordId`: 记录ID
  - 请求体:
    ```json
    {
      "subDomain": "www",
      "recordType": "A",
      "recordLine": "默认",
      "value": "192.168.1.2",
      "ttl": 600
    }
    ```
- **响应示例**:

```json
{
  "code": 200,
  "message": "修改记录成功",
  "data": {
    "recordId": 12345,
    "success": true,
    "message": "修改记录成功"
  },
  "timestamp": "2025-08-12T10:30:00Z"
}
```

### 删除记录

- **接口路径**: `DELETE /domains/{domain}/records/{recordId}`
- **描述**: 删除指定域名下的记录
- **请求参数**:
  - `domain`: 域名，如 example.com
  - `recordId`: 记录ID
- **响应示例**:

```json
{
  "code": 200,
  "message": "删除记录成功",
  "data": null,
  "timestamp": "2025-08-12T10:30:00Z"
}
```

## 记录分组管理接口

### 创建记录分组

- **接口路径**: `POST /domains/{domain}/groups`
- **描述**: 在指定域名下创建新的记录分组
- **请求参数**:
  - `domain`: 域名，如 example.com
  - 请求体:
    ```json
    {
      "groupName": "测试分组",
      "domainId": 12345  // 可选，如果提供则优先使用
    }
    ```
- **响应示例**:

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "groupId": 146,
    "success": true,
    "message": "创建记录分组成功",
    "requestId": "ec8949ba-ec3c-446e-b9eb-5aeafa238f0a"
  },
  "timestamp": "2025-08-12T10:30:00Z"
}
```

## 错误码说明

| 状态码 | 含义 | 说明 |
| ------ | ---- | ---- |
| 200 | 成功 | 请求处理成功 |
| 201 | 创建成功 | 资源创建成功 |
| 400 | 请求错误 | 参数格式错误或缺失 |
| 401 | 未授权 | 未登录或token无效 |
| 403 | 禁止访问 | 无权限执行操作 |
| 404 | 资源不存在 | 请求的资源不存在 |
| 500 | 服务器错误 | 服务器内部错误 |

## 测试示例

### 创建记录分组

```bash
curl -X POST "http://localhost:8084/api/dnspod/domains/example.com/groups" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"groupName": "测试分组"}'
```

### 获取记录列表（高级查询）

```bash
curl -X GET "http://localhost:8084/api/dnspod/domains/example.com/records/search?keyword=test&recordType=A&limit=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
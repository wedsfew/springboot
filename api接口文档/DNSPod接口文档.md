# DNSPod接口文档

## 一、接口概述

本文档描述了DNSPod相关的API接口，用于管理域名和域名记录。

### 基本信息

| 项目             | 内容                           |
| ---------------- | ------------------------------ |
| **Base URL**     | `http://localhost:8084/api/dnspod` |
| **Content-Type** | `application/json`             |
| **字符编码**     | `UTF-8`                        |

## 二、接口详情

### 2.1 获取域名列表

#### 基本信息

- **接口标识**：`DNSPOD_GET_DOMAINS`
- **请求路径**：`GET /domains`
- **接口描述**：获取用户的域名列表
- **认证要求**：需要认证（JWT令牌）

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

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
    },
    {
      "domainId": 67890,
      "name": "test.com",
      "status": "ENABLE",
      "recordCount": 3
    }
  ],
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

### 2.2 获取域名记录列表

#### 基本信息

- **接口标识**：`DNSPOD_GET_RECORDS`
- **请求路径**：`GET /domains/{domain}/records`
- **接口描述**：获取指定域名的记录列表
- **认证要求**：需要认证（JWT令牌）

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| domain | String | 域名     | 是       |

#### 响应示例

##### 成功响应（状态码 200）

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
    },
    {
      "recordId": 67890,
      "subDomain": "mail",
      "recordType": "MX",
      "recordLine": "默认",
      "value": "mail.example.com.",
      "ttl": 3600,
      "status": "ENABLE"
    }
  ],
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

### 2.3 创建域名记录

#### 基本信息

- **接口标识**：`DNSPOD_CREATE_RECORD`
- **请求路径**：`POST /domains/{domain}/records`
- **接口描述**：创建域名记录
- **认证要求**：需要认证（JWT令牌）

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| domain | String | 域名     | 是       |

#### 请求参数

```json
{
  "subDomain": "www",
  "recordType": "A",
  "recordLine": "默认",
  "value": "192.168.1.1",
  "ttl": 600
}
```

#### 参数说明

| 参数名     | 类型   | 必填 | 说明                                     |
| ---------- | ------ | ---- | ---------------------------------------- |
| subDomain  | string | 是   | 子域名，如www                            |
| recordType | string | 是   | 记录类型，如A, CNAME, MX, TXT等          |
| recordLine | string | 是   | 记录线路，如默认, 电信, 联通等           |
| value      | string | 是   | 记录值，如IP地址                         |
| ttl        | int    | 是   | 生存时间，单位秒                         |

#### 响应示例

##### 成功响应（状态码 201）

```json
{
  "code": 201,
  "message": "创建记录成功",
  "data": {
    "recordId": 12345,
    "success": true,
    "message": "创建记录成功"
  },
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

##### 失败响应（状态码 400）

```json
{
  "code": 400,
  "message": "创建记录失败: 参数错误",
  "data": null,
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

### 2.4 修改域名记录

#### 基本信息

- **接口标识**：`DNSPOD_MODIFY_RECORD`
- **请求路径**：`PUT /domains/{domain}/records/{recordId}`
- **接口描述**：修改域名记录
- **认证要求**：需要认证（JWT令牌）

#### 路径参数

| 参数名   | 类型   | 描述     | 是否必须 |
| -------- | ------ | -------- | -------- |
| domain   | String | 域名     | 是       |
| recordId | String | 记录ID   | 是       |

#### 请求参数

```json
{
  "subDomain": "www",
  "recordType": "A",
  "recordLine": "默认",
  "value": "192.168.1.2",
  "ttl": 600
}
```

#### 参数说明

与创建域名记录接口相同

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "修改记录成功",
  "data": {
    "recordId": 12345,
    "success": true,
    "message": "修改记录成功"
  },
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

##### 失败响应（状态码 400）

```json
{
  "code": 400,
  "message": "修改记录失败: 记录不存在",
  "data": null,
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

### 2.5 删除域名记录

#### 基本信息

- **接口标识**：`DNSPOD_DELETE_RECORD`
- **请求路径**：`DELETE /domains/{domain}/records/{recordId}`
- **接口描述**：删除域名记录
- **认证要求**：需要认证（JWT令牌）

#### 路径参数

| 参数名   | 类型   | 描述     | 是否必须 |
| -------- | ------ | -------- | -------- |
| domain   | String | 域名     | 是       |
| recordId | String | 记录ID   | 是       |

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "删除记录成功",
  "data": null,
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

##### 失败响应（状态码 400）

```json
{
  "code": 400,
  "message": "删除记录失败: 记录不存在",
  "data": null,
  "timestamp": "2025-08-12T06:30:00+08:00"
}
```

## 三、测试用例

### 3.1 获取域名列表测试

```bash
curl -X GET -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8084/api/dnspod/domains
```

### 3.2 获取域名记录列表测试

```bash
curl -X GET -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8084/api/dnspod/domains/example.com/records
```

### 3.3 创建域名记录测试

```bash
curl -X POST -H "Authorization: Bearer YOUR_JWT_TOKEN" -H "Content-Type: application/json" -d '{"subDomain":"www","recordType":"A","recordLine":"默认","value":"192.168.1.1","ttl":600}' http://localhost:8084/api/dnspod/domains/example.com/records
```

### 3.4 修改域名记录测试

```bash
curl -X PUT -H "Authorization: Bearer YOUR_JWT_TOKEN" -H "Content-Type: application/json" -d '{"subDomain":"www","recordType":"A","recordLine":"默认","value":"192.168.1.2","ttl":600}' http://localhost:8084/api/dnspod/domains/example.com/records/12345
```

### 3.5 删除域名记录测试

```bash
curl -X DELETE -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8084/api/dnspod/domains/example.com/records/12345
```

## 四、注意事项

1. 所有接口都需要JWT认证，请在请求头中添加`Authorization: Bearer YOUR_JWT_TOKEN`
2. 创建和修改记录时，请确保提供的参数符合DNSPod的要求
3. 在使用接口前，请确保已在application.properties中配置了正确的腾讯云SecretId和SecretKey
4. 接口返回的状态码和消息可能会根据DNSPod API的响应而变化
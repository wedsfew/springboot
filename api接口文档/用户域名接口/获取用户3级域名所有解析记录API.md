# 获取用户3级域名所有解析记录API

## 基本信息

- **接口标识**：`USER_DNS_RECORDS_QUERY`
- **请求路径**：`POST /api/user/dns-records/query`
- **接口描述**：获取指定用户3级域名的所有DNS解析记录，支持分页和条件过滤
- **认证要求**：需要JWT认证（Bearer Token）
- **适用业务单元**：用户DNS记录管理

## 请求参数

### 请求头
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

### 请求体
```typescript
interface GetDnsRecordsRequest {
  fullDomain: string;    // 完整的3级域名，如：sdae.cblog.eu
  type?: string;         // 可选：记录类型过滤（A、CNAME、MX、TXT等）
  status?: string;       // 可选：记录状态过滤（ENABLE、DISABLE）
  page?: number;         // 可选：页码，默认为1
  size?: number;         // 可选：每页大小，默认为20
}
```

### 参数验证规则
```json
{
  "fullDomain": {
    "required": true,
    "type": "string",
    "pattern": "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.[a-zA-Z]{2,}$",
    "message": "请输入有效的3级域名格式，如：subdomain.domain.com"
  },
  "type": {
    "required": false,
    "type": "string",
    "enum": ["A", "AAAA", "CNAME", "MX", "TXT", "NS", "SRV"],
    "message": "记录类型必须是有效的DNS记录类型"
  },
  "status": {
    "required": false,
    "type": "string",
    "enum": ["ENABLE", "DISABLE"],
    "message": "状态必须是ENABLE或DISABLE"
  },
  "page": {
    "required": false,
    "type": "integer",
    "minimum": 1,
    "message": "页码必须是大于0的整数"
  },
  "size": {
    "required": false,
    "type": "integer",
    "minimum": 1,
    "maximum": 100,
    "message": "每页大小必须是1-100之间的整数"
  }
}
```

## 响应示例

### 成功响应（状态码 200）

```typescript
interface GetDnsRecordsResponse {
  code: 200;
  message: "查询成功";
  data: {
    records: DnsRecordResponse[];  // DNS记录列表
    total: number;                 // 总记录数
    page: number;                  // 当前页码
    size: number;                  // 每页大小
    totalPages: number;            // 总页数
    hasNext: boolean;              // 是否有下一页
    hasPrevious: boolean;          // 是否有上一页
  };
  timestamp: string;
}

interface DnsRecordResponse {
  id: number;                    // 记录ID
  recordId?: number;             // DNSPod记录ID
  name: string;                  // 主机记录（如www、mail等，@表示主域名）
  type: string;                  // 记录类型（A、CNAME、MX、TXT等）
  value: string;                 // 记录值（如IP地址、域名等）
  line: string;                  // 记录线路
  ttl: number;                   // TTL值（生存时间）
  mx?: number;                   // MX优先级（仅MX记录需要）
  weight?: number;               // 权重（负载均衡）
  status: string;                // 记录状态（ENABLE或DISABLE）
  remark?: string;               // 备注信息
  monitorStatus?: string;        // 监控状态
  syncStatus: string;            // 同步状态（PENDING、SUCCESS、FAILED）
  updatedOn?: string;            // DNSPod更新时间
  createTime: string;            // 创建时间
  updateTime: string;            // 更新时间
}
```

**示例响应：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "recordId": 123456789,
        "name": "@",
        "type": "A",
        "value": "192.168.1.100",
        "line": "默认",
        "ttl": 600,
        "status": "ENABLE",
        "remark": "主域名A记录",
        "syncStatus": "SUCCESS",
        "createTime": "2025-08-28T10:30:00+08:00",
        "updateTime": "2025-08-28T10:30:00+08:00"
      },
      {
        "id": 2,
        "recordId": 123456790,
        "name": "www",
        "type": "CNAME",
        "value": "sdae.cblog.eu",
        "line": "默认",
        "ttl": 600,
        "status": "ENABLE",
        "remark": "WWW别名记录",
        "syncStatus": "SUCCESS",
        "createTime": "2025-08-28T11:00:00+08:00",
        "updateTime": "2025-08-28T11:00:00+08:00"
      }
    ],
    "total": 2,
    "page": 1,
    "size": 20,
    "totalPages": 1,
    "hasNext": false,
    "hasPrevious": false
  },
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

### 失败响应

#### 3级域名不存在（404）
```json
{
  "code": 404,
  "message": "3级域名不存在",
  "data": null,
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

#### 无权限访问（403）
```json
{
  "code": 403,
  "message": "无权限访问该3级域名的解析记录",
  "data": null,
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

#### 域名状态异常（400）
```json
{
  "code": 400,
  "message": "3级域名状态异常，无法查询解析记录",
  "data": null,
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

#### 参数验证失败（400）
```json
{
  "code": 400,
  "message": "参数验证失败，请检查输入",
  "data": {
    "errors": [
      {
        "field": "fullDomain",
        "message": "请输入有效的3级域名格式，如：subdomain.domain.com"
      }
    ]
  },
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

#### 未授权（401）
```json
{
  "code": 401,
  "message": "未授权，请先登录",
  "data": null,
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

#### 服务器错误（500）
```json
{
  "code": 500,
  "message": "获取DNS解析记录失败: 系统内部错误",
  "data": null,
  "timestamp": "2025-08-28T13:45:00+08:00"
}
```

## 业务逻辑说明

### 处理流程
1. **身份验证**：验证JWT token的有效性，获取用户ID
2. **域名验证**：验证用户是否拥有指定的3级域名
3. **状态检查**：确认3级域名状态为ACTIVE
4. **数据查询**：从数据库查询该域名下的所有DNS解析记录
5. **条件过滤**：根据type和status参数过滤记录
6. **分页处理**：按照page和size参数进行分页
7. **数据转换**：将实体对象转换为响应DTO
8. **返回结果**：返回分页的DNS记录列表

### 权限控制
- 用户只能查询自己拥有的3级域名的解析记录
- 域名状态必须为ACTIVE才能查询解析记录
- 通过JWT token验证用户身份

### 分页说明
- 默认每页返回20条记录
- 最大每页100条记录
- 返回分页元信息：总记录数、总页数、是否有上下页等

## 测试用例

### 测试用例1：成功查询所有记录
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {valid_token}" \
  -d '{
    "fullDomain": "sdae.cblog.eu",
    "page": 1,
    "size": 20
  }'
```
**预期响应：** code=200，返回该域名下的所有DNS记录

### 测试用例2：按类型过滤查询
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {valid_token}" \
  -d '{
    "fullDomain": "sdae.cblog.eu",
    "type": "A",
    "page": 1,
    "size": 10
  }'
```
**预期响应：** code=200，只返回A记录类型的DNS记录

### 测试用例3：域名不存在
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {valid_token}" \
  -d '{
    "fullDomain": "nonexistent.cblog.eu"
  }'
```
**预期响应：** code=404，message="3级域名不存在"

### 测试用例4：无权限访问
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {valid_token}" \
  -d '{
    "fullDomain": "other-user.cblog.eu"
  }'
```
**预期响应：** code=403，message="无权限访问该3级域名的解析记录"

### 测试用例5：参数验证失败
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {valid_token}" \
  -d '{
    "fullDomain": "invalid-domain"
  }'
```
**预期响应：** code=400，参数验证失败

### 测试用例6：未授权访问
**请求：**
```bash
curl -X POST http://localhost:8080/api/user/dns-records/query \
  -H "Content-Type: application/json" \
  -d '{
    "fullDomain": "sdae.cblog.eu"
  }'
```
**预期响应：** code=401，message="未授权，请先登录"

## 注意事项

1. **域名格式**：必须是有效的3级域名格式，如：subdomain.domain.com
2. **分页限制**：每页最多返回100条记录，建议使用默认的20条
3. **过滤条件**：type和status参数都是可选的，可以单独使用或组合使用
4. **权限控制**：用户只能查询自己拥有的域名记录
5. **状态要求**：只有ACTIVE状态的域名才能查询解析记录
6. **同步状态**：返回的记录包含与DNSPod的同步状态信息

## 相关接口

- [用户登录API](../认证接口/用户登录API.md)
- [添加DNS解析记录API](./添加用户DNS解析记录API.md)
- [修改DNS解析记录API](./修改用户DNS解析记录API.md)
- [删除DNS解析记录API](./用户删除3级域名解析记录API.md)
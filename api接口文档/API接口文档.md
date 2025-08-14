# API接口文档

## 一、API 合约概述

### 1.1 合约目标

定义前后端交互的标准接口规范，明确接口设计、参数格式、响应逻辑等核心要素，确保前后端开发的一致性、可测试性及协作效率。

### 1.2 合约原则

- **接口优先**：API 设计先行于代码实现，所有开发基于已确认的接口合约开展
- **版本管理**：支持 API 版本迭代，版本变更需同步更新合约并通知相关方
- **标准化**：统一请求 / 响应格式、数据类型、状态码等基础规范
- **可测试**：每个接口包含明确的测试用例（输入、预期输出），支持独立验证
- **可追溯**：合约变更需记录版本号、修改内容、责任人及时间，便于回溯

## 二、通用规范

### 2.1 基础信息

| 项目             | 内容                           | 说明                                                         |
| ---------------- | ------------------------------ | ------------------------------------------------------------ |
| **Base URL**     | `http://localhost:8080/api/v1` | 接口基础路径，版本号（v1）需随重大变更升级                   |
| **Content-Type** | `application/json`             | 统一采用 JSON 格式传输数据                                   |
| **字符编码**     | `UTF-8`                        | 确保多语言字符兼容                                           |
| **时间格式**     | `ISO 8601`                     | 示例：`2025-01-09T10:30:00Z`（UTC 时间）、`2025-01-09T18:30:00+08:00`（带时区） |
| **请求方法**     | 遵循 RESTful 规范              | GET（查询）、POST（创建）、PUT（全量更新）、PATCH（部分更新）、DELETE（删除） |

### 2.2 统一响应格式

所有API接口均使用统一的响应格式，结构如下：

```json
{
  "code": 200,           // 业务状态码
  "message": "操作成功",   // 响应描述信息
  "data": { ... },       // 响应数据（成功时为业务数据，失败时可为null或错误详情）
  "timestamp": "2025-08-11T00:30:00+08:00"  // 服务器响应时间戳（ISO 8601格式）
}
```

### 2.3 状态码定义

| 状态码 | 含义       | 适用场景                     | 前端处理建议                                |
| ------ | ---------- | ---------------------------- | ------------------------------------------- |
| 200    | 成功       | 请求处理完成且结果正常       | 解析 data 展示业务数据                      |
| 201    | 创建成功   | 资源（如用户、订单）创建完成 | 可跳转至详情页或提示创建结果                |
| 400    | 请求错误   | 参数格式错误、缺失必填项等   | 展示具体参数错误信息（如`data.errors`）     |
| 401    | 未授权     | 未登录、token 过期或无效     | 跳转至登录页                                |
| 403    | 禁止访问   | 登录状态下无操作权限         | 提示 "无权限"，可跳转至首页                 |
| 404    | 资源不存在 | 请求的 URL 或资源 ID 不存在  | 提示 "资源不存在"                           |
| 409    | 资源冲突   | 资源已存在（如用户名重复）   | 提示冲突原因（如 "用户名已被注册"）         |
| 423    | 账户锁定   | 账户因安全原因被临时锁定     | 提示锁定原因及解锁方式                      |
| 500    | 服务器错误 | 后端逻辑异常、数据库错误等   | 提示 "系统繁忙，请稍后重试"，不展示技术细节 |

## 三、用户管理接口

### 3.1 获取所有用户

#### 基本信息

- **接口标识**：`USER_GET_ALL`
- **请求路径**：`GET /api/users`
- **接口描述**：获取系统中所有用户的列表
- **认证要求**：需要认证（后续实现）
- **适用业务单元**：用户管理模块

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTBpKLt2YmZIQNFmNgxdxPq0dWdUkJIy",
      "email": "admin@example.com"
    },
    {
      "id": 2,
      "username": "user1",
      "password": "$2a$10$8CFFt.FeopdF3jCQUwCkd.eCDmr2K0aeKgFnQtLgEccgyL4.luoFq",
      "email": "user1@example.com"
    }
  ],
  "timestamp": "2025-08-11T00:11:11.572155"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/users`
  - 预期响应：code=200，data包含用户列表

### 3.2 获取单个用户

#### 基本信息

- **接口标识**：`USER_GET_BY_ID`
- **请求路径**：`GET /api/users/{id}`
- **接口描述**：根据用户ID获取用户详细信息
- **认证要求**：需要认证（后续实现）
- **适用业务单元**：用户管理模块

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| id     | Long   | 用户ID   | 是       |

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTBpKLt2YmZIQNFmNgxdxPq0dWdUkJIy",
    "email": "admin@example.com"
  },
  "timestamp": "2025-08-11T00:11:20.804578"
}
```

##### 用户不存在响应（状态码 404）

```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

#### 调试说明

- **测试用例 1（成功场景）**：
  - 请求：`curl -X GET http://localhost:8080/api/users/1`
  - 预期响应：code=200，data包含用户信息
- **测试用例 2（失败场景）**：
  - 请求：`curl -X GET http://localhost:8080/api/users/999`
  - 预期响应：code=404，message="用户不存在"

### 3.3 创建用户

#### 基本信息

- **接口标识**：`USER_CREATE`
- **请求路径**：`POST /api/users`
- **接口描述**：创建新用户
- **认证要求**：需要认证（后续实现）
- **适用业务单元**：用户管理模块

#### 请求参数

```json
{
  "username": "newuser",
  "password": "Password123",
  "email": "newuser@example.com"
}
```

#### 参数验证规则

| 参数名   | 是否必须 | 类型   | 验证规则                                   | 错误消息                                       |
| -------- | -------- | ------ | ------------------------------------------ | ---------------------------------------------- |
| username | 是       | string | 3-20字符，仅含字母、数字、下划线           | 用户名只能包含字母、数字、下划线，长度3-20字符 |
| password | 是       | string | 8-32字符，必须包含大小写字母和数字         | 密码必须包含大小写字母和数字，长度8-32字符     |
| email    | 是       | string | 符合标准邮箱格式（如xxx@xx.com）           | 请输入有效的邮箱地址                           |

#### 响应示例

##### 成功响应（状态码 201）

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 3,
    "username": "newuser",
    "password": "$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "email": "newuser@example.com"
  },
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

##### 用户名已存在响应（状态码 409）

```json
{
  "code": 409,
  "message": "用户名已被注册，请更换",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

##### 参数验证失败响应（状态码 400）

```json
{
  "code": 400,
  "message": "参数验证失败，请检查输入",
  "data": {
    "errors": [
      {
        "field": "username",
        "message": "用户名只能包含字母、数字、下划线"
      },
      {
        "field": "password",
        "message": "密码必须包含大小写字母和数字"
      }
    ]
  },
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

#### 调试说明

- **测试用例 1（成功场景）**：
  - 请求：`curl -X POST -H "Content-Type: application/json" -d '{"username":"newuser","password":"Password123","email":"newuser@example.com"}' http://localhost:8080/api/users`
  - 预期响应：code=201，data包含新创建的用户信息
- **测试用例 2（失败场景 - 用户名重复）**：
  - 请求：`curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"Password123","email":"another@example.com"}' http://localhost:8080/api/users`
  - 预期响应：code=409，message包含用户名已存在的提示

### 3.4 更新用户

#### 基本信息

- **接口标识**：`USER_UPDATE`
- **请求路径**：`PUT /api/users/{id}`
- **接口描述**：更新指定ID的用户信息
- **认证要求**：需要认证（后续实现）
- **适用业务单元**：用户管理模块

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| id     | Long   | 用户ID   | 是       |

#### 请求参数

```json
{
  "username": "updateduser",
  "password": "NewPassword123",
  "email": "updated@example.com"
}
```

#### 参数验证规则

与创建用户接口相同

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "用户更新成功",
  "data": {
    "id": 1,
    "username": "updateduser",
    "password": "$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "email": "updated@example.com"
  },
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

##### 用户不存在响应（状态码 404）

```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

#### 调试说明

- **测试用例 1（成功场景）**：
  - 请求：`curl -X PUT -H "Content-Type: application/json" -d '{"username":"updateduser","password":"NewPassword123","email":"updated@example.com"}' http://localhost:8080/api/users/1`
  - 预期响应：code=200，message="用户更新成功"，data包含更新后的用户信息
- **测试用例 2（失败场景）**：
  - 请求：`curl -X PUT -H "Content-Type: application/json" -d '{"username":"updateduser","password":"NewPassword123","email":"updated@example.com"}' http://localhost:8080/api/users/999`
  - 预期响应：code=404，message="用户不存在"

### 3.5 删除用户

#### 基本信息

- **接口标识**：`USER_DELETE`
- **请求路径**：`DELETE /api/users/{id}`
- **接口描述**：删除指定ID的用户
- **认证要求**：需要认证（后续实现）
- **适用业务单元**：用户管理模块

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| id     | Long   | 用户ID   | 是       |

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "用户删除成功",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

##### 用户不存在响应（状态码 404）

```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

#### 调试说明

- **测试用例 1（成功场景）**：
  - 请求：`curl -X DELETE http://localhost:8080/api/users/2`
  - 预期响应：code=200，message="用户删除成功"
- **测试用例 2（失败场景）**：
  - 请求：`curl -X DELETE http://localhost:8080/api/users/999`
  - 预期响应：code=404，message="用户不存在"

## 四、系统测试接口

### 4.1 健康检查接口

#### 基本信息

- **接口标识**：`HEALTH_CHECK`
- **请求路径**：`GET /health`
- **接口描述**：检查应用是否正常运行
- **认证要求**：无需认证
- **适用业务单元**：系统监控

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "UP",
    "message": "应用正常运行中"
  },
  "timestamp": "2025-08-11T00:11:02.52634"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/health`
  - 预期响应：code=200，data包含应用状态信息

## 五、API测试接口

### 5.1 成功响应测试

#### 基本信息

- **接口标识**：`TEST_SUCCESS`
- **请求路径**：`GET /api/test/success`
- **接口描述**：测试成功响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "操作成功",
  "data": "这是一个成功的响应",
  "timestamp": "2025-08-11T00:11:30.192994"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/success`
  - 预期响应：code=200，data为字符串消息

### 5.2 带数据的成功响应测试

#### 基本信息

- **接口标识**：`TEST_DATA`
- **请求路径**：`GET /api/test/data`
- **接口描述**：测试带数据的成功响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "获取数据成功",
  "data": {
    "name": "测试用户",
    "age": 25,
    "email": "test@example.com"
  },
  "timestamp": "2025-08-11T00:11:39.000488"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/data`
  - 预期响应：code=200，message="获取数据成功"，data包含用户信息

### 5.3 创建资源响应测试

#### 基本信息

- **接口标识**：`TEST_CREATE`
- **请求路径**：`POST /api/test/create`
- **接口描述**：测试创建资源响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 201）

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 100,
    "username": "newUser",
    "password": "password123",
    "email": "new@example.com"
  },
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X POST http://localhost:8080/api/test/create`
  - 预期响应：code=201，message="创建成功"，data包含新创建的用户信息

### 5.4 列表数据响应测试

#### 基本信息

- **接口标识**：`TEST_LIST`
- **请求路径**：`GET /api/test/list`
- **接口描述**：测试列表数据响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 成功响应（状态码 200）

```json
{
  "code": 200,
  "message": "操作成功",
  "data": ["项目1", "项目2", "项目3"],
  "timestamp": "2025-08-11T00:11:48.321178"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/list`
  - 预期响应：code=200，data为字符串数组

### 5.5 业务异常测试

#### 基本信息

- **接口标识**：`TEST_BUSINESS_ERROR`
- **请求路径**：`GET /api/test/business-error`
- **接口描述**：测试业务异常响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 失败响应（状态码 400）

```json
{
  "code": 400,
  "message": "业务处理失败：参数不符合业务规则",
  "data": null,
  "timestamp": "2025-08-11T00:11:57.9533"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/business-error`
  - 预期响应：code=400，message包含业务错误信息

### 5.6 资源未找到异常测试

#### 基本信息

- **接口标识**：`TEST_NOT_FOUND`
- **请求路径**：`GET /api/test/not-found/{id}`
- **接口描述**：测试资源未找到异常响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 路径参数

| 参数名 | 类型   | 描述     | 是否必须 |
| ------ | ------ | -------- | -------- |
| id     | Long   | 资源ID   | 是       |

#### 响应示例

##### 失败响应（状态码 404）

```json
{
  "code": 404,
  "message": "ID为123的资源不存在",
  "data": null,
  "timestamp": "2025-08-11T00:12:07.078423"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/not-found/123`
  - 预期响应：code=404，message包含资源不存在的提示

### 5.7 服务器错误测试

#### 基本信息

- **接口标识**：`TEST_SERVER_ERROR`
- **请求路径**：`GET /api/test/server-error`
- **接口描述**：测试服务器错误响应
- **认证要求**：无需认证
- **适用业务单元**：API测试

#### 请求参数

无

#### 响应示例

##### 失败响应（状态码 500）

```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": "服务器内部错误测试",
  "timestamp": "2025-08-11T00:12:18.240028"
}
```

#### 调试说明

- **测试用例**：
  - 请求：`curl -X GET http://localhost:8080/api/test/server-error`
  - 预期响应：code=500，message="服务器内部错误"

## 六、接口测试方法

### 6.1 使用curl命令行工具

可以使用curl命令行工具来测试API接口，例如：

```bash
# 获取所有用户
curl -X GET http://localhost:8080/api/users

# 获取单个用户
curl -X GET http://localhost:8080/api/users/1

# 创建用户
curl -X POST -H "Content-Type: application/json" -d '{"username":"newuser","password":"Password123","email":"newuser@example.com"}' http://localhost:8080/api/users

# 更新用户
curl -X PUT -H "Content-Type: application/json" -d '{"username":"updateduser","password":"NewPassword123","email":"updated@example.com"}' http://localhost:8080/api/users/1

# 删除用户
curl -X DELETE http://localhost:8080/api/users/2

# 健康检查
curl -X GET http://localhost:8080/health

# 测试成功响应
curl -X GET http://localhost:8080/api/test/success
```

### 6.2 使用Postman等API测试工具

1. 打开Postman
2. 创建一个新的请求
3. 输入请求URL（如`http://localhost:8083/api/users`）
4. 选择请求方法（GET、POST、PUT、DELETE等）
5. 对于POST和PUT请求，在Body选项卡中选择raw和JSON格式，输入请求体
6. 点击Send按钮发送请求
7. 查看响应结果

### 6.3 使用浏览器测试GET接口

对于GET类型的接口，可以直接在浏览器地址栏中输入URL进行测试，例如：

```
http://localhost:8080/health
http://localhost:8080/api/users
http://localhost:8080/api/users/1
http://localhost:8080/api/test/success
```

## 七、错误处理

所有API接口都使用统一的错误处理机制，通过GlobalExceptionHandler全局异常处理器来捕获和处理异常，确保返回统一格式的错误响应。

### 7.1 常见错误类型

1. **业务异常（BusinessException）**
   - 用于表示业务逻辑错误，如参数不符合业务规则
   - 对应状态码：400

2. **资源未找到异常（ResourceNotFoundException）**
   - 用于表示请求的资源不存在
   - 对应状态码：404

3. **服务器错误（RuntimeException及其子类）**
   - 用于表示服务器内部错误
   - 对应状态码：500

### 7.2 错误响应示例

```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null,
  "timestamp": "2025-08-11T00:30:00+08:00"
}
```

## 八、版本历史

| 版本号 | 更新日期   | 更新内容                           | 责任人    |
| ------ | ---------- | ---------------------------------- | --------- |
| v1.0.0 | 2025-08-11 | 初始版本，包含用户管理和测试接口   | CodeBuddy |
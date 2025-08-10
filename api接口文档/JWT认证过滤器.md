# JWT认证过滤器

## 概述

JWT（JSON Web Token）认证过滤器用于验证请求中的令牌，确保只有经过认证的用户才能访问受保护的API端点。

## 实现组件

### 1. JWT工具类 (JwtUtil)

负责生成、解析和验证JWT令牌。

```java
public class JwtUtil {
    // 生成令牌
    public String generateToken(Long userId) { ... }
    
    // 从令牌中获取用户ID
    public Long getUserIdFromToken(String token) { ... }
    
    // 验证令牌
    public Boolean validateToken(String token, Long userId) { ... }
}
```

### 2. JWT认证过滤器 (JwtAuthenticationFilter)

拦截所有请求，检查Authorization头中的JWT令牌，验证令牌有效性，并设置认证信息。

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取Authorization头
        // 提取JWT令牌
        // 从令牌中提取用户ID
        // 验证令牌
        // 设置认证信息
        // 继续过滤器链
    }
}
```

### 3. 用户详情服务 (UserDetailsServiceImpl)

实现Spring Security的UserDetailsService接口，用于加载用户信息。

```java
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        // 尝试将principal解析为用户ID
        // 如果不是数字，则尝试通过邮箱或用户名查找
        // 加载用户信息
        // 返回UserDetails对象
    }
}
```

### 4. JWT认证入口点 (JwtAuthenticationEntryPoint)

处理未认证的请求，返回统一的错误响应。

```java
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 设置响应状态码为401
        // 返回统一的错误响应
    }
}
```

### 5. 安全配置 (WebSecurityConfig)

配置HTTP安全规则，添加JWT认证过滤器，设置认证入口点。

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                // 配置公开接口和需要认证的接口
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

## 认证流程

1. 用户通过登录接口获取JWT令牌（令牌中包含用户ID）
2. 用户在后续请求中，在Authorization头中携带JWT令牌（格式：Bearer {token}）
3. JWT认证过滤器拦截请求，从令牌中提取用户ID
4. 如果令牌有效，设置认证信息，允许访问受保护的API端点
5. 如果令牌无效或不存在，返回401错误

## 使用用户ID的优势

1. **唯一性**：用户ID是数据库中的主键，具有唯一性，避免了用户名可能重复的问题
2. **稳定性**：即使用户更改用户名，JWT令牌仍然有效，因为用户ID不会改变
3. **安全性**：减少了在令牌中存储可能被猜测的信息（如用户名）
4. **性能**：通过用户ID直接查询数据库，避免了额外的查询步骤

## API端点保护

### 公开接口（无需认证）

- `/api/auth/**` - 认证相关接口
- `/api/verification/**` - 验证码相关接口

### 受保护接口（需要认证）

- `/api/users/**` - 用户相关接口
- 其他所有请求

## 使用示例

### 1. 登录获取令牌

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

响应：

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "user",
    "email": "user@example.com"
  },
  "timestamp": "2025-08-11T10:30:00Z"
}
```

### 2. 使用令牌访问受保护资源

```http
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com"
    },
    {
      "id": 2,
      "username": "user2",
      "email": "user2@example.com"
    }
  ],
  "timestamp": "2025-08-11T10:35:00Z"
}
```

### 3. 未认证访问受保护资源

```http
GET /api/users
```

响应：

```json
{
  "code": 401,
  "message": "未授权：Full authentication is required to access this resource",
  "data": null,
  "timestamp": "2025-08-11T10:40:00Z"
}
```

## 测试

使用JwtAuthenticationTest类测试JWT认证过滤器的功能：

1. 测试未认证访问受保护资源
2. 测试使用有效令牌访问受保护资源
3. 测试使用无效令牌访问受保护资源

使用JwtTokenIdTest类测试JWT令牌中使用用户ID作为标识符：

1. 测试生成包含用户ID的JWT令牌
2. 测试从JWT令牌中提取用户ID
3. 测试使用用户ID验证JWT令牌

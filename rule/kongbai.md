## 本次任务
创建查询域名是否已注册接口
### 业务流程
匹配 user_subdomain表   `subdomain` 和`domain` 字段,
{
    "domain":"cblog.eu"
    "subdomain":"www"
}若数据库存在则返回域名xxx.xxx已注册，否则返回域名未注册
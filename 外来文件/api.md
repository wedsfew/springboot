1. 接口描述
接口请求域名： dnspod.tencentcloudapi.com 。

修改记录

2. 输入参数
以下请求参数列表仅列出了接口请求参数和部分公共参数，完整公共参数列表见 公共请求参数。

参数名称	必选	类型	描述
Action	是	String	公共参数，本接口取值：ModifyRecord。
Version	是	String	公共参数，本接口取值：2021-03-23。
Region	否	String	公共参数，本接口不需要传递此参数。
Domain	是	String	域名
示例值：dnspod.cn
RecordType	是	String	记录类型，可通过接口DescribeRecordType获得，大写英文，比如：A 。
示例值：A
RecordLine	是	String	记录线路，可以通过接口DescribeRecordLineList查看当前域名允许的线路信息。比如：默认。
示例值：默认
Value	是	String	记录值，如 IP : 200.200.200.200， CNAME : cname.dnspod.com.， MX : mail.dnspod.com.。
示例值：200.200.200.200
RecordId	是	Integer	记录 ID 。可以通过接口DescribeRecordList查到所有的解析记录列表以及对应的RecordId
示例值：162
DomainId	否	Integer	域名 ID 。参数 DomainId 优先级比参数 Domain 高，如果传递参数 DomainId 将忽略参数 Domain 。可以通过接口DescribeDomainList查到所有的Domain以及DomainId
示例值：1923
SubDomain	否	String	主机记录，如 www，如果不传，默认为 @。
示例值：www
RecordLineId	否	String	线路的 ID，可以通过接口DescribeRecordLineList查看当前域名允许的线路信息，比如：10=1。参数RecordLineId优先级高于RecordLine，如果同时传递二者，优先使用RecordLineId参数。
示例值：10=1
MX	否	Integer	MX 优先级，当记录类型是 MX、HTTPS、SVCB 时必填，范围1-65535。
示例值：10
TTL	否	Integer	TTL，范围1-604800，不同等级域名最小值不同。
示例值：600
Weight	否	Integer	权重信息，0到100的整数。0 表示关闭，不传该参数，表示不设置权重信息。
示例值：20
Status	否	String	记录初始状态，取值范围为 ENABLE 和 DISABLE 。默认为 ENABLE ，如果传入 DISABLE，解析不会生效，也不会验证负载均衡的限制。
示例值：ENABLE
Remark	否	String	记录的备注信息。传空删除备注。
示例值：这是备注
DnssecConflictMode	否	String	开启DNSSEC时，强制将其它记录修改为CNAME/URL记录
示例值：force
3. 输出参数
参数名称	类型	描述
RecordId	Integer	记录ID
示例值：162
RequestId	String	唯一请求 ID，由服务端生成，每次请求都会返回（若请求因其他原因未能抵达服务端，则该次请求不会获得 RequestId）。定位问题时需要提供该次请求的 RequestId。
4. 示例
示例1 修改记录
输入示例
POST / HTTP/1.1
Host: dnspod.tencentcloudapi.com
Content-Type: application/json
X-TC-Action: ModifyRecord
<公共请求参数>

{
    "Domain": "dnspod.cn",
    "DomainId": 62,
    "SubDomain": "bbbb",
    "RecordType": "A",
    "RecordLine": "默认",
    "RecordLineId": "0",
    "Value": "129.23.32.32",
    "MX": 0,
    "TTL": 600,
    "Weight": 10,
    "Status": "DISABLE",
    "RecordId": 162
}
输出示例
{
    "Response": {
        "RequestId": "ab4f1426-ea15-42ea-8183-dc1b44151166",
        "RecordId": 162
    }
}
5. 错误码
以下仅列出了接口业务逻辑相关的错误码，其他错误码详见 公共错误码。

错误码	描述
FailedOperation	操作失败。
FailedOperation.DNSSECIncompleteClosed	DNSSEC 未完全关闭，不允许添加 @ 子域名 CNAME、显性 URL 或者隐性 URL 记录。
FailedOperation.DomainIsLocked	锁定域名不能进行此操作。
FailedOperation.DomainIsSpam	封禁域名不能进行此操作。
FailedOperation.FrequencyLimit	您操作过于频繁，请稍后重试
FailedOperation.LoginAreaNotAllowed	账号异地登录，请求被拒绝。
FailedOperation.LoginFailed	登录失败，请检查账号和密码是否正确。
FailedOperation.MustAddDefaultLineFirst	请先添加默认线路的解析记录
FailedOperation.UnknowError	操作未响应，请稍后重试。
InvalidParameter.AccountIsBanned	您的账号已被系统封禁，如果您有任何疑问请与我们联系。
InvalidParameter.CustomMessage	自定义错误信息。
InvalidParameter.DnssecAddCnameError	该域名开启了 DNSSEC，不允许添加 @ 子域名 CNAME、显性 URL 或者隐性 URL 记录。
InvalidParameter.DomainIdInvalid	域名编号不正确。
InvalidParameter.DomainInvalid	域名不正确，请输入主域名，如 dnspod.cn。
InvalidParameter.DomainIsAliaser	此域名是其它域名的别名。
InvalidParameter.DomainNotAllowedModifyRecords	处于生效中/失效中的域名，不允许变更解析记录。
InvalidParameter.DomainNotBeian	该域名未备案，无法添加 URL 记录。
InvalidParameter.DomainRecordExist	记录已经存在，无需再次添加。
InvalidParameter.EmailNotVerified	抱歉，您的账户还没有通过邮箱验证。
InvalidParameter.InvalidWeight	权重不合法。请输入0~100的整数。
InvalidParameter.LoginTokenIdError	Token 的 ID 不正确。
InvalidParameter.LoginTokenNotExists	传入的 Token 不存在。
InvalidParameter.LoginTokenValidateFailed	Token 验证失败。
InvalidParameter.MobileNotVerified	抱歉，您的账户还没有通过手机验证。
InvalidParameter.MxInvalid	MX优先级不正确。
InvalidParameter.OperateFailed	操作失败，请稍后再试。
InvalidParameter.RecordIdInvalid	记录编号错误。
InvalidParameter.RecordLineInvalid	记录线路不正确。
InvalidParameter.RecordTypeInvalid	记录类型不正确。
InvalidParameter.RecordValueInvalid	记录的值不正确。
InvalidParameter.RecordValueLengthInvalid	解析记录值过长。
InvalidParameter.RequestIpLimited	您的IP非法，请求被拒绝。
InvalidParameter.SubdomainInvalid	子域名不正确。
InvalidParameter.UnrealNameUser	未实名认证用户，请先完成实名认证再操作。
InvalidParameter.UrlValueIllegal	很抱歉，您要添加的URL的内容不符合DNSPod解析服务条款，URL添加/启用失败，如需帮助请联系技术支持。
InvalidParameter.UserNotExists	用户不存在。
InvalidParameterValue.DomainNotExists	当前域名有误，请返回重新操作。
InvalidParameterValue.UserIdInvalid	用户编号不正确。
LimitExceeded.AAAACountLimit	AAAA记录数量超出限制。
LimitExceeded.AtNsRecordLimit	@的NS记录只能设置为默认线路。
LimitExceeded.FailedLoginLimitExceeded	登录失败次数过多已被系统封禁。
LimitExceeded.HiddenUrlExceeded	该域名使用的套餐不支持隐性URL转发或数量已达上限，如需要使用，请去商城购买。
LimitExceeded.NsCountLimit	NS记录数量超出限制。
LimitExceeded.RecordTtlLimit	记录的TTL值超出了限制。
LimitExceeded.SrvCountLimit	SRV记录数量超出限制。
LimitExceeded.SubdomainLevelLimit	子域名级数超出限制。
LimitExceeded.SubdomainRollLimit	子域名负载均衡数量超出限制。
LimitExceeded.SubdomainWcardLimit	泛解析级数超出限制。
LimitExceeded.UrlCountLimit	该域名的显性URL转发数量已达上限，如需继续使用，请去商城购买。
OperationDenied.DomainOwnerAllowedOnly	仅域名所有者可进行此操作。
OperationDenied.IPInBlacklistNotAllowed	抱歉，不允许添加黑名单中的IP。
OperationDenied.NoPermissionToOperateDomain	当前域名无权限，请返回域名列表。
OperationDenied.NotAdmin	您不是管理用户。
OperationDenied.NotAgent	您不是代理用户。
OperationDenied.NotManagedUser	不是您名下用户。
RequestLimitExceeded.RequestLimitExceeded	API请求次数超出限制。
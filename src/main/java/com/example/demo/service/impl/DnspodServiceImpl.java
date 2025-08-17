package com.example.demo.service.impl;

import com.example.demo.service.DnspodService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.CreateRecordRequest;
import com.tencentcloudapi.dnspod.v20210323.models.CreateRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DeleteRecordRequest;
import com.tencentcloudapi.dnspod.v20210323.models.DeleteRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListRequest;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordRequest;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 文件名：DnspodServiceImpl.java
 * 功能：腾讯云DNSPod服务实现类，提供域名解析记录查询功能
 * 作者：CodeBuddy
 * 创建时间：2025-08-16
 * 版本：v1.0.0
 */
@Service
public class DnspodServiceImpl implements DnspodService {

    @Value("${tencent.cloud.secret-id:your-secret-id}")
    private String secretId;

    @Value("${tencent.cloud.secret-key:your-secret-key}")
    private String secretKey;

    @Value("${tencent.cloud.region:ap-beijing}")
    private String region;

    /**
     * 获取域名的解析记录筛选列表
     * 
     * @param domain 域名，如 example.com
     * @param remark 备注信息（可选）
     * @param subDomain 子域名（可选）
     * @param recordType 记录类型（可选）
     * @param limit 限制数量（可选）
     * @param offset 偏移量（可选）
     * @return 解析记录列表响应
     */
    @Override
    public DescribeRecordFilterListResponse getRecordFilterList(String domain, String remark, 
                                                               String subDomain, String recordType,
                                                               Integer limit, Integer offset) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
            Credential cred = new Credential(secretId, secretKey);
            
            // 实例化一个http选项
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象
            DnspodClient client = new DnspodClient(cred, region, clientProfile);
            
            // 实例化一个请求对象
            DescribeRecordFilterListRequest req = new DescribeRecordFilterListRequest();
            req.setDomain(domain);
            
            // 设置可选参数
            if (remark != null && !remark.isEmpty()) {
                req.setRemark(remark);
            }
            if (subDomain != null && !subDomain.isEmpty()) {
                req.setSubDomain(subDomain);
            }
            if (recordType != null && !recordType.isEmpty()) {
                req.setRecordType(new String[]{recordType});
            }
            if (limit != null) {
                req.setLimit(limit.longValue());
            }
            if (offset != null) {
                req.setOffset(offset.longValue());
            }
            
            // 返回的resp是一个DescribeRecordFilterListResponse的实例
            return client.DescribeRecordFilterList(req);
            
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("调用腾讯云DNSPod API失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 添加域名解析记录
     * 
     * @param domain 域名，如 example.com
     * @param recordType 记录类型，如 A、CNAME、MX等
     * @param recordLine 记录线路，如 "默认"
     * @param value 记录值，如 IP地址
     * @param subDomain 主机记录，如 www（可选，默认为@）
     * @param ttl TTL值（可选，默认600）
     * @param mx MX优先级（可选，MX记录时必填）
     * @param weight 权重（可选，0-100）
     * @param status 记录状态（可选，ENABLE或DISABLE）
     * @param remark 备注（可选）
     * @return 创建记录响应
     */
    @Override
    public CreateRecordResponse createRecord(String domain, String recordType, String recordLine, 
                                           String value, String subDomain, Long ttl, Long mx, 
                                           Long weight, String status, String remark) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
            Credential cred = new Credential(secretId, secretKey);
            
            // 实例化一个http选项
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象
            DnspodClient client = new DnspodClient(cred, region, clientProfile);
            
            // 实例化一个请求对象
            CreateRecordRequest req = new CreateRecordRequest();
            req.setDomain(domain);
            req.setRecordType(recordType);
            req.setRecordLine(recordLine != null ? recordLine : "默认");
            req.setValue(value);
            
            // 设置可选参数
            if (subDomain != null && !subDomain.isEmpty()) {
                req.setSubDomain(subDomain);
            }
            if (ttl != null) {
                req.setTTL(ttl);
            } else {
                req.setTTL(600L); // 默认TTL为600秒
            }
            if (mx != null) {
                req.setMX(mx);
            }
            if (weight != null) {
                req.setWeight(weight);
            }
            if (status != null && !status.isEmpty()) {
                req.setStatus(status);
            } else {
                req.setStatus("ENABLE"); // 默认启用
            }
            if (remark != null && !remark.isEmpty()) {
                req.setRemark(remark);
            }
            
            // 返回的resp是一个CreateRecordResponse的实例
            return client.CreateRecord(req);
            
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("调用腾讯云DNSPod创建记录API失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除域名解析记录
     * 
     * @param domain 域名，如 dnspod.cn
     * @param recordId 记录 ID，可以通过接口DescribeRecordList查到所有的解析记录列表以及对应的RecordId
     * @param domainId 域名 ID（可选），参数 DomainId 优先级比参数 Domain 高
     * @return 删除记录响应
     */
    @Override
    public DeleteRecordResponse deleteRecord(String domain, Long recordId, Long domainId) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
            Credential cred = new Credential(secretId, secretKey);
            
            // 实例化一个http选项
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象
            DnspodClient client = new DnspodClient(cred, region, clientProfile);
            
            // 实例化一个请求对象
            DeleteRecordRequest req = new DeleteRecordRequest();
            req.setDomain(domain);
            req.setRecordId(recordId);
            
            // 设置可选参数
            if (domainId != null) {
                req.setDomainId(domainId);
            }
            
            // 返回的resp是一个DeleteRecordResponse的实例
            return client.DeleteRecord(req);
            
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("调用腾讯云DNSPod删除记录API失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 修改域名解析记录
     * 
     * @param requestDto 修改记录请求参数
     * @return 修改记录响应
     */
    @Override
    public ModifyRecordResponse modifyRecord(com.example.demo.dto.ModifyRecordRequest requestDto) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
            Credential cred = new Credential(secretId, secretKey);
            
            // 实例化一个http选项
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象
            DnspodClient client = new DnspodClient(cred, region, clientProfile);
            
            // 实例化一个请求对象
            ModifyRecordRequest req = new ModifyRecordRequest();
            
            // 设置必填参数
            req.setDomain(requestDto.getDomain());
            req.setRecordType(requestDto.getRecordType());
            req.setRecordLine(requestDto.getRecordLine());
            req.setValue(requestDto.getValue());
            req.setRecordId(requestDto.getRecordId());
            
            // 设置可选参数
            if (requestDto.getDomainId() != null) {
                req.setDomainId(requestDto.getDomainId());
            }
            
            if (requestDto.getSubDomain() != null && !requestDto.getSubDomain().isEmpty()) {
                req.setSubDomain(requestDto.getSubDomain());
            }
            
            if (requestDto.getRecordLineId() != null && !requestDto.getRecordLineId().isEmpty()) {
                req.setRecordLineId(requestDto.getRecordLineId());
            }
            
            if (requestDto.getMx() != null) {
                req.setMX(requestDto.getMx().longValue());
            }
            
            if (requestDto.getTtl() != null) {
                req.setTTL(requestDto.getTtl().longValue());
            }
            
            if (requestDto.getWeight() != null) {
                req.setWeight(requestDto.getWeight().longValue());
            }
            
            if (requestDto.getStatus() != null && !requestDto.getStatus().isEmpty()) {
                req.setStatus(requestDto.getStatus());
            }
            
            // 注意：腾讯云DNSPod SDK的ModifyRecordRequest可能不支持setRemark和setDnssecConflictMode方法
            // 如果需要这些功能，请查阅最新的SDK文档
            
            // 返回的resp是一个ModifyRecordResponse的实例
            return client.ModifyRecord(req);
            
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("调用腾讯云DNSPod修改记录API失败: " + e.getMessage(), e);
        }
    }
}

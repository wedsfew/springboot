package com.example.demo.service.impl;

import com.example.demo.service.DnspodService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListRequest;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordFilterListResponse;
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

    @Value("${tencent.cloud.secret-id}")
    private String secretId;

    @Value("${tencent.cloud.secret-key}")
    private String secretKey;

    @Value("${tencent.cloud.region}")
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
}
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;

/**
 * 文件名：TencentCloudConfig.java
 * 功能：腾讯云SDK配置类，提供腾讯云API客户端实例
 * 作者：CodeBuddy
 * 创建时间：2025-08-12
 * 版本：v1.0.0
 */
@Configuration
public class TencentCloudConfig {

    @Value("${tencent.cloud.secret-id:}")
    private String secretId;

    @Value("${tencent.cloud.secret-key:}")
    private String secretKey;

    @Value("${tencent.cloud.region:ap-guangzhou}")
    private String region;

    /**
     * 创建腾讯云凭证对象
     * @return Credential 腾讯云凭证对象
     */
    @Bean
    public Credential tencentCredential() {
        return new Credential(secretId, secretKey);
    }

    /**
     * 创建DNSPod客户端
     * @param credential 腾讯云凭证
     * @return DnspodClient DNSPod客户端
     */
    @Bean
    public DnspodClient dnspodClient(Credential credential) {
        // 实例化一个HTTP选项，可选，无特殊需求时可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("dnspod.tencentcloudapi.com");

        // 实例化一个客户端配置对象，可选，无特殊需求时可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        // 实例化要请求产品的client对象，clientProfile是可选的
        return new DnspodClient(credential, region, clientProfile);
    }
}
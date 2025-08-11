// 文件名：DNSPodConfig.java
// 功能：腾讯云DNSPod API客户端配置类
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：配置腾讯云API客户端，使用项目中已配置的SecretId和SecretKey

package com.example.demo.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云DNSPod API客户端配置类
 * 提供DNSPod客户端实例的配置和创建
 */
@Configuration
public class DNSPodConfig {

    /**
     * 腾讯云SecretId（从环境变量获取）
     */
    private static final String SECRET_ID = System.getenv("TENCENT_CLOUD_SECRET_ID");

    /**
     * 腾讯云SecretKey（从环境变量获取）
     */
    private static final String SECRET_KEY = System.getenv("TENCENT_CLOUD_SECRET_KEY");

    /**
     * 腾讯云地域，DNSPod服务不区分地域，使用默认值
     */
    private static final String REGION = "";

    /**
     * 创建DNSPod客户端实例
     * @return DNSPod客户端
     */
    @Bean
    public DnspodClient dnspodClient() {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象，clientProfile是可选的
            DnspodClient client = new DnspodClient(cred, REGION, clientProfile);
            
            return client;
        } catch (Exception e) {
            throw new RuntimeException("初始化DNSPod客户端失败", e);
        }
    }
}
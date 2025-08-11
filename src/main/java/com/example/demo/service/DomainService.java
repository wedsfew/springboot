// 文件名：DomainService.java
// 功能：域名服务接口，定义域名相关的业务操作
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：提供域名管理的业务逻辑接口

package com.example.demo.service;

import com.example.demo.entity.Domain;

import java.util.List;

/**
 * 域名服务接口
 * 定义域名相关的业务操作方法
 */
public interface DomainService {

    /**
     * 从腾讯云DNSPod获取域名列表
     * @return 域名信息列表
     * @throws Exception 当API调用失败时抛出异常
     */
    List<Domain> getDomainListFromDNSPod() throws Exception;

    /**
     * 获取本地数据库中的所有域名信息
     * @return 域名信息列表
     */
    List<Domain> getAllDomains();

    /**
     * 根据域名ID获取域名信息
     * @param domainId 腾讯云域名ID
     * @return 域名信息，如果不存在返回null
     */
    Domain getDomainByDomainId(Long domainId);

    /**
     * 根据域名名称获取域名信息
     * @param domainName 域名名称
     * @return 域名信息，如果不存在返回null
     */
    Domain getDomainByDomainName(String domainName);

    /**
     * 保存域名信息到数据库
     * @param domain 域名信息
     * @return 保存后的域名信息
     */
    Domain saveDomain(Domain domain);

    /**
     * 更新域名信息
     * @param domain 域名信息
     * @return 更新后的域名信息
     */
    Domain updateDomain(Domain domain);

    /**
     * 删除域名信息
     * @param domainId 腾讯云域名ID
     * @return 是否删除成功
     */
    boolean deleteDomain(Long domainId);

    /**
     * 同步域名数据：从腾讯云DNSPod获取最新域名列表并更新到数据库
     * @return 同步结果信息
     * @throws Exception 当同步过程中发生错误时抛出异常
     */
    String syncDomainsFromDNSPod() throws Exception;

    /**
     * 批量保存域名信息
     * @param domains 域名信息列表
     * @return 保存成功的数量
     */
    int batchSaveDomains(List<Domain> domains);
}
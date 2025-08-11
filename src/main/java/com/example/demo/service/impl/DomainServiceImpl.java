// 文件名：DomainServiceImpl.java
// 功能：域名服务实现类，实现域名相关的业务操作
// 作者：系统生成
// 创建时间：2025-01-11
// 版本：v1.0.0
// 备注：实现域名管理的业务逻辑，包括腾讯云DNSPod API调用和数据库操作

package com.example.demo.service.impl;

import com.example.demo.entity.Domain;
import com.example.demo.mapper.DomainMapper;
import com.example.demo.service.DomainService;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeDomainListRequest;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeDomainListResponse;
import com.tencentcloudapi.dnspod.v20210323.models.DomainListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 域名服务实现类
 * 实现域名相关的业务操作，包括腾讯云DNSPod API调用和数据库操作
 */
@Slf4j
@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private DnspodClient dnspodClient;

    /**
     * 从腾讯云DNSPod获取域名列表
     * @return 域名信息列表
     * @throws Exception 当API调用失败时抛出异常
     */
    @Override
    public List<Domain> getDomainListFromDNSPod() throws Exception {
        log.info("开始从腾讯云DNSPod获取域名列表");
        
        try {
            // 实例化一个请求对象
            DescribeDomainListRequest req = new DescribeDomainListRequest();
            
            // 返回的resp是一个DescribeDomainListResponse的实例，与请求对象对应
            DescribeDomainListResponse resp = dnspodClient.DescribeDomainList(req);
            
            // 转换腾讯云响应数据为本地Domain实体
            List<Domain> domains = new ArrayList<>();
            if (resp.getDomainList() != null) {
                for (DomainListItem item : resp.getDomainList()) {
                    Domain domain = convertToDomain(item);
                    domains.add(domain);
                }
            }
            
            log.info("成功从腾讯云DNSPod获取到{}个域名", domains.size());
            return domains;
            
        } catch (Exception e) {
            log.error("从腾讯云DNSPod获取域名列表失败", e);
            throw new Exception("获取域名列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将腾讯云域名信息转换为本地Domain实体
     * @param item 腾讯云域名信息
     * @return 本地Domain实体
     */
    private Domain convertToDomain(DomainListItem item) {
        Domain domain = new Domain();
        domain.setDomainId(item.getDomainId());
        domain.setDomainName(item.getName());
        domain.setStatus(item.getStatus());
        domain.setGrade(item.getGrade());
        domain.setGradeTitle(item.getGradeTitle());
        
        // 使用腾讯云SDK中实际存在的方法和字段
        domain.setIsMark(false); // 默认值，腾讯云SDK中可能没有此字段
        domain.setTtl(item.getTTL() != null ? item.getTTL().intValue() : 600);
        domain.setDnsStatus(item.getDNSStatus());
        
        // 这些字段在腾讯云SDK的DomainListItem中不存在，使用默认值
        domain.setMinTtl(600); // 设置默认最小TTL
        domain.setRecordCount(0); // 设置默认记录数，后续可通过其他API获取
        
        // 处理时间字段
        if (item.getCreatedOn() != null && !item.getCreatedOn().isEmpty()) {
            domain.setCreatedOn(parseDateTime(item.getCreatedOn()));
        }
        if (item.getUpdatedOn() != null && !item.getUpdatedOn().isEmpty()) {
            domain.setUpdatedOn(parseDateTime(item.getUpdatedOn()));
        }
        
        domain.setOwner(item.getOwner());
        domain.setRemark(item.getRemark());
        
        return domain;
    }

    /**
     * 解析时间字符串为LocalDateTime
     * @param dateTimeStr 时间字符串
     * @return LocalDateTime对象
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // 腾讯云返回的时间格式通常为：2023-01-01 12:00:00
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            log.warn("解析时间字符串失败: {}", dateTimeStr, e);
            return null;
        }
    }

    /**
     * 获取本地数据库中的所有域名信息
     * @return 域名信息列表
     */
    @Override
    public List<Domain> getAllDomains() {
        log.info("获取本地数据库中的所有域名信息");
        return domainMapper.findAll();
    }

    /**
     * 根据域名ID获取域名信息
     * @param domainId 腾讯云域名ID
     * @return 域名信息，如果不存在返回null
     */
    @Override
    public Domain getDomainByDomainId(Long domainId) {
        log.info("根据域名ID获取域名信息: {}", domainId);
        return domainMapper.findByDomainId(domainId);
    }

    /**
     * 根据域名名称获取域名信息
     * @param domainName 域名名称
     * @return 域名信息，如果不存在返回null
     */
    @Override
    public Domain getDomainByDomainName(String domainName) {
        log.info("根据域名名称获取域名信息: {}", domainName);
        return domainMapper.findByDomainName(domainName);
    }

    /**
     * 保存域名信息到数据库
     * @param domain 域名信息
     * @return 保存后的域名信息
     */
    @Override
    @Transactional
    public Domain saveDomain(Domain domain) {
        log.info("保存域名信息到数据库: {}", domain.getDomainName());
        
        // 检查域名是否已存在
        Domain existingDomain = domainMapper.findByDomainId(domain.getDomainId());
        if (existingDomain != null) {
            // 如果存在，则更新
            domainMapper.updateByDomainId(domain);
            return domainMapper.findByDomainId(domain.getDomainId());
        } else {
            // 如果不存在，则插入
            domainMapper.insert(domain);
            return domain;
        }
    }

    /**
     * 更新域名信息
     * @param domain 域名信息
     * @return 更新后的域名信息
     */
    @Override
    @Transactional
    public Domain updateDomain(Domain domain) {
        log.info("更新域名信息: {}", domain.getDomainName());
        domainMapper.updateByDomainId(domain);
        return domainMapper.findByDomainId(domain.getDomainId());
    }

    /**
     * 删除域名信息
     * @param domainId 腾讯云域名ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteDomain(Long domainId) {
        log.info("删除域名信息: {}", domainId);
        int result = domainMapper.deleteByDomainId(domainId);
        return result > 0;
    }

    /**
     * 同步域名数据：从腾讯云DNSPod获取最新域名列表并更新到数据库
     * @return 同步结果信息
     * @throws Exception 当同步过程中发生错误时抛出异常
     */
    @Override
    @Transactional
    public String syncDomainsFromDNSPod() throws Exception {
        log.info("开始同步域名数据从腾讯云DNSPod");
        
        try {
            // 从腾讯云获取域名列表
            List<Domain> cloudDomains = getDomainListFromDNSPod();
            
            int newCount = 0;
            int updateCount = 0;
            
            // 逐个处理域名信息
            for (Domain cloudDomain : cloudDomains) {
                Domain existingDomain = domainMapper.findByDomainId(cloudDomain.getDomainId());
                
                if (existingDomain == null) {
                    // 新域名，插入数据库
                    domainMapper.insert(cloudDomain);
                    newCount++;
                    log.info("新增域名: {}", cloudDomain.getDomainName());
                } else {
                    // 已存在域名，更新信息
                    domainMapper.updateByDomainId(cloudDomain);
                    updateCount++;
                    log.info("更新域名: {}", cloudDomain.getDomainName());
                }
            }
            
            String result = String.format("域名同步完成，新增: %d个，更新: %d个，总计: %d个", 
                                        newCount, updateCount, cloudDomains.size());
            log.info(result);
            return result;
            
        } catch (Exception e) {
            log.error("同步域名数据失败", e);
            throw new Exception("同步域名数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量保存域名信息
     * @param domains 域名信息列表
     * @return 保存成功的数量
     */
    @Override
    @Transactional
    public int batchSaveDomains(List<Domain> domains) {
        log.info("批量保存{}个域名信息", domains.size());
        
        if (domains == null || domains.isEmpty()) {
            return 0;
        }
        
        return domainMapper.batchInsert(domains);
    }
}
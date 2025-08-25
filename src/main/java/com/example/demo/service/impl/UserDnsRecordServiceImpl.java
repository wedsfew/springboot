package com.example.demo.service.impl;

import com.example.demo.entity.UserDnsRecord;
import com.example.demo.mapper.UserDnsRecordMapper;
import com.example.demo.service.UserDnsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户DNS解析记录服务实现类
 * 提供用户DNS解析记录的业务逻辑处理
 * 
 * @author CodeBuddy
 * @since 2025-08-25
 */
@Service
public class UserDnsRecordServiceImpl implements UserDnsRecordService {
    
    @Autowired
    private UserDnsRecordMapper userDnsRecordMapper;
    
    @Override
    public UserDnsRecord createRecord(UserDnsRecord record) {
        // 检查记录是否已存在
        if (userDnsRecordMapper.existsByUserIdAndSubdomainIdAndNameAndType(
                record.getUserId(), record.getSubdomainId(), record.getName(), record.getType()) > 0) {
            throw new RuntimeException("DNS解析记录已存在");
        }
        
        userDnsRecordMapper.insert(record);
        return record;
    }
    
    @Override
    public UserDnsRecord getRecordById(Long id) {
        return userDnsRecordMapper.selectById(id);
    }
    
    @Override
    public List<UserDnsRecord> getRecordsByUserId(Long userId) {
        return userDnsRecordMapper.selectByUserId(userId);
    }
    
    @Override
    public List<UserDnsRecord> getRecordsBySubdomainId(Long subdomainId) {
        return userDnsRecordMapper.selectBySubdomainId(subdomainId);
    }
    
    @Override
    public List<UserDnsRecord> getRecordsByUserIdAndSubdomainId(Long userId, Long subdomainId) {
        return userDnsRecordMapper.selectByUserIdAndSubdomainId(userId, subdomainId);
    }
    
    @Override
    public UserDnsRecord getRecordByRecordId(Long recordId) {
        return userDnsRecordMapper.selectByRecordId(recordId);
    }
    
    @Override
    public List<UserDnsRecord> getRecordsBySyncStatus(String syncStatus) {
        return userDnsRecordMapper.selectBySyncStatus(syncStatus);
    }
    
    @Override
    public UserDnsRecord updateRecord(UserDnsRecord record) {
        userDnsRecordMapper.update(record);
        return record;
    }
    
    @Override
    public void updateSyncStatus(Long id, String syncStatus, String syncError) {
        userDnsRecordMapper.updateSyncStatus(id, syncStatus, syncError);
    }
    
    @Override
    public void updateRecordId(Long id, Long recordId) {
        userDnsRecordMapper.updateRecordId(id, recordId);
    }
    
    @Override
    public void deleteRecord(Long id) {
        userDnsRecordMapper.deleteById(id);
    }
    
    @Override
    public void deleteRecordsByUserId(Long userId) {
        userDnsRecordMapper.deleteByUserId(userId);
    }
    
    @Override
    public void deleteRecordsBySubdomainId(Long subdomainId) {
        userDnsRecordMapper.deleteBySubdomainId(subdomainId);
    }
    
    @Override
    public int countRecordsByUserId(Long userId) {
        return userDnsRecordMapper.countByUserId(userId);
    }
    
    @Override
    public int countRecordsBySubdomainId(Long subdomainId) {
        return userDnsRecordMapper.countBySubdomainId(subdomainId);
    }
    
    @Override
    public boolean recordExists(Long userId, Long subdomainId, String name, String type) {
        return userDnsRecordMapper.existsByUserIdAndSubdomainIdAndNameAndType(userId, subdomainId, name, type) > 0;
    }
}
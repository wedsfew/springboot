#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import time
import sys

# 配置参数
API_BASE_URL = "http://localhost:8080"
LOGIN_ENDPOINT = "/api/auth/login"
REGISTER_SUBDOMAIN_ENDPOINT = "/api/user/domains/register"
CHECK_SUBDOMAIN_ENDPOINT = "/api/dnspod/available-subdomain"

# 测试用户凭据
TEST_EMAIL = "12345678@example.com"
TEST_PASSWORD = "12345678"

# 测试域名参数
SUBDOMAIN = f"test{int(time.time())}"  # 使用时间戳确保唯一性
IP_ADDRESS = "8.8.8.8"

def main():
    print("=== 用户注册三级域名接口测试 ===")
    print(f"测试时间: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"测试子域名: {SUBDOMAIN}")
    
    # 步骤1: 登录获取JWT令牌
    print("\n步骤1: 登录获取JWT令牌")
    login_data = {
        "email": TEST_EMAIL,
        "password": TEST_PASSWORD
    }
    
    try:
        login_response = requests.post(
            f"{API_BASE_URL}{LOGIN_ENDPOINT}",
            json=login_data,
            headers={"Content-Type": "application/json"}
        )
        
        print(f"登录响应状态码: {login_response.status_code}")
        login_json = login_response.json()
        print(f"登录响应: {json.dumps(login_json, indent=2)}")
        
        if login_response.status_code != 200 or login_json.get("code") != 200:
            print("登录失败!")
            return
        
        # 提取JWT令牌
        jwt_token = login_json.get("data", {}).get("token")
        if not jwt_token:
            print("无法提取JWT令牌!")
            return
        
        print(f"JWT令牌: {jwt_token[:20]}...")
        
        # 步骤2: 注册三级域名
        print("\n步骤2: 注册三级域名")
        register_data = {
            "subDomain": SUBDOMAIN,
            "value": IP_ADDRESS
        }
        
        register_response = requests.post(
            f"{API_BASE_URL}{REGISTER_SUBDOMAIN_ENDPOINT}",
            json=register_data,
            headers={
                "Content-Type": "application/json",
                "Authorization": f"Bearer {jwt_token}"
            }
        )
        
        print(f"注册响应状态码: {register_response.status_code}")
        try:
            register_json = register_response.json()
            print(f"注册响应: {json.dumps(register_json, indent=2)}")
        except:
            print(f"注册响应: {register_response.text}")
        
        # 步骤3: 验证域名是否已注册
        print("\n步骤3: 验证域名是否已注册")
        check_response = requests.get(
            f"{API_BASE_URL}{CHECK_SUBDOMAIN_ENDPOINT}",
            params={"subDomain": SUBDOMAIN},
            headers={"Authorization": f"Bearer {jwt_token}"}
        )
        
        print(f"验证响应状态码: {check_response.status_code}")
        try:
            check_json = check_response.json()
            print(f"验证响应: {json.dumps(check_json, indent=2)}")
        except:
            print(f"验证响应: {check_response.text}")
        
    except Exception as e:
        print(f"测试过程中出现错误: {e}")
    
    print("\n=== 测试完成 ===")

if __name__ == "__main__":
    main()
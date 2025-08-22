#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
文件名：test_domain_suffix_validation.py
功能：测试域名后缀验证功能
作者：CodeBuddy
创建时间：2025-08-22
版本：v1.0.0
"""

import requests
import json
import time
import random
import sys

# 配置信息
BASE_URL = "http://localhost:8080"
LOGIN_URL = f"{BASE_URL}/api/auth/login"
DOMAIN_SUFFIXES_URL = f"{BASE_URL}/api/domains/suffixes"
REGISTER_SUBDOMAIN_URL = f"{BASE_URL}/api/user/domains/register"

# 测试用户信息
TEST_USER = {
    "email": "12345678@example.com",
    "password": "12345678"
}

# 生成随机子域名前缀
def generate_random_subdomain():
    timestamp = int(time.time())
    random_num = random.randint(1000, 9999)
    return f"test{timestamp}{random_num}"

# 格式化输出JSON
def print_json(json_obj):
    print(json.dumps(json_obj, indent=2, ensure_ascii=False))

# 登录并获取JWT令牌
def login():
    print("\n===== 步骤1: 用户登录 =====")
    response = requests.post(LOGIN_URL, json=TEST_USER)
    
    if response.status_code != 200:
        print(f"登录失败，状态码: {response.status_code}")
        print_json(response.json())
        sys.exit(1)
    
    login_data = response.json()
    print("登录成功！")
    print_json(login_data)
    
    if login_data["code"] != 200 or "token" not in login_data["data"]:
        print("登录响应格式错误或登录失败")
        sys.exit(1)
    
    return login_data["data"]["token"]

# 获取可用域名后缀
def get_domain_suffixes():
    print("\n===== 步骤2: 获取可用域名后缀 =====")
    response = requests.get(DOMAIN_SUFFIXES_URL)
    
    if response.status_code != 200:
        print(f"获取域名后缀失败，状态码: {response.status_code}")
        print_json(response.json())
        sys.exit(1)
    
    suffixes_data = response.json()
    print("获取域名后缀成功！")
    print_json(suffixes_data)
    
    if suffixes_data["code"] != 200 or "data" not in suffixes_data:
        print("获取域名后缀响应格式错误")
        sys.exit(1)
    
    return suffixes_data["data"]

# 测试有效域名后缀
def test_valid_domain_suffix(token, domain_suffix):
    print(f"\n===== 步骤3: 测试有效域名后缀 {domain_suffix} =====")
    
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    subdomain = generate_random_subdomain()
    data = {
        "subDomain": subdomain,
        "domain": domain_suffix,
        "value": "8.8.8.8",
        "ttl": 600
    }
    
    print(f"请求数据: {data}")
    response = requests.post(REGISTER_SUBDOMAIN_URL, headers=headers, json=data)
    print(f"响应状态码: {response.status_code}")
    
    try:
        response_data = response.json()
        print_json(response_data)
        return response_data
    except:
        print(f"响应内容: {response.text}")
        return None

# 测试无效域名后缀
def test_invalid_domain_suffix(token):
    print("\n===== 步骤4: 测试无效域名后缀 invalid.domain.com =====")
    
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    subdomain = generate_random_subdomain()
    data = {
        "subDomain": subdomain,
        "domain": "invalid.domain.com",
        "value": "8.8.8.8",
        "ttl": 600
    }
    
    print(f"请求数据: {data}")
    response = requests.post(REGISTER_SUBDOMAIN_URL, headers=headers, json=data)
    print(f"响应状态码: {response.status_code}")
    
    try:
        response_data = response.json()
        print_json(response_data)
        return response_data
    except:
        print(f"响应内容: {response.text}")
        return None

# 主函数
def main():
    print("===== 域名后缀验证功能测试 =====")
    print(f"当前时间: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    
    # 1. 登录获取JWT令牌
    token = login()
    
    # 2. 获取可用域名后缀
    domain_suffixes = get_domain_suffixes()
    
    if not domain_suffixes:
        print("没有可用的域名后缀，测试终止")
        sys.exit(1)
    
    # 3. 测试有效域名后缀
    valid_suffix = domain_suffixes[0]
    valid_response = test_valid_domain_suffix(token, valid_suffix)
    
    # 4. 测试无效域名后缀
    invalid_response = test_invalid_domain_suffix(token)
    
    # 5. 测试结果总结
    print("\n===== 测试结果总结 =====")
    print(f"有效域名后缀测试: {'通过' if valid_response and valid_response.get('code') == 200 else '失败'}")
    print(f"无效域名后缀测试: {'通过' if invalid_response and invalid_response.get('code') == 400 else '失败'}")
    
    print("\n===== 测试完成 =====")

if __name__ == "__main__":
    main()
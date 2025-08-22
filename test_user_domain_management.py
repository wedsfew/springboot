#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
文件名：test_user_domain_management.py
功能：测试用户三级域名管理接口
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
REGISTER_SUBDOMAIN_URL = f"{BASE_URL}/api/user/domains/register"
LIST_SUBDOMAINS_URL = f"{BASE_URL}/api/user/domains/subdomains"
DELETE_SUBDOMAIN_URL = f"{BASE_URL}/api/user/domains/subdomains"

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

# 注册三级域名
def register_subdomain(token, subdomain):
    print(f"\n===== 步骤2: 注册三级域名 {subdomain}.cblog.eu =====")
    
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    data = {
        "subDomain": subdomain,
        "value": "8.8.8.8",
        "ttl": 600
    }
    
    response = requests.post(REGISTER_SUBDOMAIN_URL, headers=headers, json=data)
    print(f"注册响应状态码: {response.status_code}")
    
    try:
        response_data = response.json()
        print_json(response_data)
        return response_data
    except:
        print(f"响应内容: {response.text}")
        return None

# 查询用户三级域名列表
def list_subdomains(token):
    print("\n===== 步骤3: 查询用户三级域名列表 =====")
    
    headers = {
        "Authorization": f"Bearer {token}"
    }
    
    response = requests.get(LIST_SUBDOMAINS_URL, headers=headers)
    print(f"查询响应状态码: {response.status_code}")
    
    try:
        response_data = response.json()
        print_json(response_data)
        return response_data
    except:
        print(f"响应内容: {response.text}")
        return None

# 删除用户三级域名
def delete_subdomain(token, subdomain_id):
    print(f"\n===== 步骤4: 删除用户三级域名 (ID: {subdomain_id}) =====")
    
    headers = {
        "Authorization": f"Bearer {token}"
    }
    
    response = requests.delete(f"{DELETE_SUBDOMAIN_URL}/{subdomain_id}", headers=headers)
    print(f"删除响应状态码: {response.status_code}")
    
    try:
        response_data = response.json()
        print_json(response_data)
        return response_data
    except:
        print(f"响应内容: {response.text}")
        return None

# 主函数
def main():
    print("===== 用户三级域名管理接口测试 =====")
    print(f"当前时间: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    
    # 1. 登录获取JWT令牌
    token = login()
    
    # 2. 注册三级域名
    subdomain = generate_random_subdomain()
    register_response = register_subdomain(token, subdomain)
    
    # 3. 查询用户三级域名列表
    list_response = list_subdomains(token)
    
    # 4. 如果有域名记录，尝试删除第一个
    if list_response and list_response["code"] == 200 and list_response["data"] and len(list_response["data"]) > 0:
        subdomain_id = list_response["data"][0]["id"]
        delete_response = delete_subdomain(token, subdomain_id)
        
        # 5. 再次查询确认删除结果
        print("\n===== 步骤5: 再次查询确认删除结果 =====")
        list_response_after_delete = list_subdomains(token)
    
    print("\n===== 测试完成 =====")

if __name__ == "__main__":
    main()
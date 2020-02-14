package com.han.createbankrecfileapi.model.config;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    /**
     * 用户ID
     */
    public String UserId;
    /**
     * 用户IP地址
     */
    public List<String> IpAddress;
}

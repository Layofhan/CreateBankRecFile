package com.han.createbankrecfileapi.dto;

import lombok.Data;

//用户信息表
@Data
public class UserInfo {
    /**
     * 主键
     */
    private int Urid;
    /**
     * 用户ID
     */
    private String UserId;
    /**
     * 用户ip地址
     */
    private String IpAddress;
}

package com.han.createbankrecfileapi.dto;

import lombok.Data;

//程序的配置信息
@Data
public class ProgramInfo {
    /**
     * 主键 自增长
     */
    private int Urid;
    /**
     * 程序开启的端口
     */
    private String Port;
    /**
     * 程序开启的ip
     */
    private String Ip;
}

package com.han.createbankrecfileapi.dto;

import lombok.Data;

//随机返回结果表
@Data
public class SingleTransReturnResultRandom {
    /**
     * 主键
     */
    private int Urid;
    /**
     * 客户方银行编码
     */
    private String CustBankCode;
    /**
     * 交易状态
     */
    private String State;
    /**
     * 信息码
     */
    private String Code;
    /**
     * 交易信息
     */
    private String Info;
    /**
     * 用户ID
     */
    private String UserId;
    /**
     * 权重
     */
    private Integer Weight;
}

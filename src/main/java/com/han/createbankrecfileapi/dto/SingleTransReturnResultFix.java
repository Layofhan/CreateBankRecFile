package com.han.createbankrecfileapi.dto;

import lombok.Data;

//固定返回结果表
@Data
public class SingleTransReturnResultFix {
    /**
     * 主键
     */
    private int Urid;
    /**
     * 险企号
     */
    private String EnterpriseNum;
    /**
     * 客户方账号
     */
    private String CustAccNum;
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
}

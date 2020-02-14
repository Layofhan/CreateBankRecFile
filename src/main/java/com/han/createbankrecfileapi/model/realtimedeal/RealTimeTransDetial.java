package com.han.createbankrecfileapi.model.realtimedeal;

import lombok.Data;

@Data
public class RealTimeTransDetial {
    /**
     * 流水号
     */
    private String TransNo;
    /**
     *协议号
     * 签约协议编号
     */
    private String ProtocolCode;
    /**
     * 企业方账号
     */
    private String EnterpriseAccNum;
    /**
     * 客户方渠道代码
     */
    private String CustBankCode;
    /**
     * 客户方账号
     */
    private String CustAccNum;
    /**
     * 客户方户名
     */
    private String CustAccName;
    /**
     * 区域码
     */
    private String AreaCode;
    /**
     * 银行区域码
     */
    private String BankLocationCode;
    /**
     * 银行区域名称
     */
    private String BankLocationName;
    /**
     * 卡类型
     */
    private String CardType;
    /**
     * 加密标记
     */
    private String IsPrivate;
    /**
     *
     */
    private String IsUrgent;
    /**
     * 金额
     */
    private String Amount;
    /**
     * 币种
     */
    private String Currency;
    /**
     *  证件类型
     */
    private String CertType;
    /**
     *  证件号码
     */
    private String CertNum;
    /**
     * 手机号码
     */
    private String Mobile;
    /**
     * 用途
     */
    private String Purpose;
    /**
     * 备注
     */
    private String Memo;
    /**
     * 对账码
     */
    private String ReconciliationCode;
    /**
     * 保单号
     */
    private String PolicyNumber;
    /**
     * 扩展字段1
     */
    private String Extent1;
    /**
     * 扩展字段2
     */
    private String Extent2;
    /**
     * 原始流水号
     */
    private String SourceTransNo;
    //#############################
    /**
     * 返回报文中的返回状态(成功2，失败3，未知-1)
     */
    private String state;
    /**
     * 返回报文中的返回码
     */
    private String infoCode;
    /**
     * 返回报文中的返回信息
     */
    private String info;
}

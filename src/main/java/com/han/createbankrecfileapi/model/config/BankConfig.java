package com.han.createbankrecfileapi.model.config;

import lombok.Data;

@Data
public class BankConfig {
    /**
     * 私钥模量
     */
    private String PrivateKeyModulus;
    /**
     * 私钥指数
     */
    private String PrivateKeyExponent;
    /**
     * 私钥P
     */
    private String p;
    /**
     * 私钥Q
     */
    private String Q;
    /**
     * 私钥DP
     */
    private String DP;
    /**
     * 私钥DQ
     */
    private String DQ;
    /**
     * 私钥InverseQ
     */
    private String InverseQ;
    /**
     * 私钥D
     */
    private String D;
    /**
     * 公钥模量
     */
    private String PublicKeyModulus;
    /**
     * 公钥指数
     */
    private String PublicKeyExponent;
}

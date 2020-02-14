package com.han.createbankrecfileapi.dto;

import lombok.Data;
import org.springframework.web.util.pattern.PathPattern;

//银行公私钥
@Data
public class BankSoftKey {
    /**
     * 主键自增长
     */
    private int Urid;
    /**
     * 银行代码
     */
    private String BankCode;
    /**
     * 公钥
     */
    private String PublicKey;
    /**
     * 用户id
     */
    private String UserId;
    /**
     * 私钥
     */
    private String PrivateKey;

    private String P;

    private String Q;

    private String DP;

    private String DQ;

    private String InverseQ;

    private String D;
}

package com.han.createbankrecfileapi.model.realtimedeal;

import com.han.createbankrecfileapi.model.Head;
import lombok.Data;

@Data
public class RealTimeSingleTransHead extends Head {
    /**
     * 指令类型
     */
    private String commandCode;
    /**
     * 时间戳
     */
    private String transSeqID;
    /**
     * 验签码
     */
    private String verifyCode;
    /**
     * 压缩类型
     */
    private String zipType;
    /**
     *  企业方渠道代码
     */
    private String corpBankCode;
    /**
     *
     */
    private String fGCommandCode;
    /**
     * 险企号
     */
    private String enterpriseNum;
    /**
     *
     */
    private String transKeyEncryptFlag;
    /**
     * 版本号
     */
    private String version;
    // ##########################
    /**
     * 响应码(成功失败返回0，未知返回-1)
     */
    private String respCode;
    /**
     * 响应信息(成功失败可选填，位置返回==>模拟未知)
     */
    private String respInfo;
}

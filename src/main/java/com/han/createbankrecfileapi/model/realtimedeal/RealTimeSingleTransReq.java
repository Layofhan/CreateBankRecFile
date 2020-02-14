package com.han.createbankrecfileapi.model.realtimedeal;

import com.han.createbankrecfileapi.model.Req;
import lombok.Data;

@Data
public class RealTimeSingleTransReq extends Req {
    /**
     * 交易类型
     */
    private String MoneyWay;
    /**
     * 交易日期
     */
    private String TransDate;
    /**
     * 交易报文
     */
    private RealTimeTransDetial Trans;
    /**
     * 业务类型
     */
    private String BusType;
    /**
     * 记账标记
     * 1：单笔记账
     * 3：日终汇总记账
     */
    private String AccountingFlag;
}

package com.han.createbankrecfileapi.controller;

import com.han.createbankrecfileapi.common.helper.CommandCodeEnum;
import com.han.createbankrecfileapi.common.helper.ConfigHelper;
import com.han.createbankrecfileapi.singlemoudle.RealTimeSingleTrans;
import com.han.createbankrecfileapi.singlemoudle.SingleTest;
import com.han.createbankrecfileapi.singlemoudle.SingleTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForWard {

    @Autowired
    private SingleTrans singleTrans;
    @Autowired
    private RealTimeSingleTrans realTimeSingleTrans;
    @Autowired
    private SingleTest singleTest;

    public String Deal(String p_CommonCode,String p_Xml,String p_RequestIp){
        try {
            switch (CommandCodeEnum.getByValue(p_CommonCode)){
                case RealTimeSingleTrans: //单笔实时交易
                    singleTrans = realTimeSingleTrans;
                    break;
                case SingleTest: //
                    singleTrans = singleTest;
                    break;
                default:
                    return "<Root><Head><RespCode>-1</RespCode><RespInfo>指令不存在</RespInfo></Head></Root>";
            }
            //获取对应的配置信息
            singleTrans.setUserConfig(ConfigHelper.GetInstance().GetUserConfigsByIp(p_RequestIp));

            return singleTrans.deal(p_Xml);
        }catch (Exception ex){
            return "<Root><Head><RespCode>-1</RespCode><RespInfo>转发出现问题</RespInfo></Head></Root>";
        }
    }
}

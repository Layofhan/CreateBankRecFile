package com.han.createbankrecfileapi.singlemoudle;

import com.han.createbankrecfileapi.common.helper.ConfigHelper;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultFix;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultRandom;
import com.han.createbankrecfileapi.dto.UserInfo;
import com.han.createbankrecfileapi.model.config.UserConfig;
import com.han.createbankrecfileapi.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleTest extends SingleTrans{
    @Autowired
    private UserConfigService userConfigService;
    @Override
    public String deal(String p_Xml){
        try {
            //获取配置
            UserConfig userConfigone = super.getUserConfig();
            UserConfig userConfig = ConfigHelper.GetInstance().GetUserConfigsByIp("127.0.0.1");
            UserConfig userConfigTwo = ConfigHelper.GetInstance().GetUserConfigsByIp("127.0.0.2");
            //用户信息
            List<UserInfo> userInfosLi = userConfigService.selectUserInfoAll();
            List<UserInfo> userInfoOne = userConfigService.selectUserInfoById("123");
            UserInfo userInfoTwo = userConfigService.selectUserInfoByUrid("1");
            //返回结果 - 固定
            List<SingleTransReturnResultFix> singleTransReturnResultFixes = userConfigService.selectResFixAll();
            SingleTransReturnResultFix singleTransReturnResultFix = userConfigService.selectResFixByEnterAndCustAcc("QT33001", "1234");
            SingleTransReturnResultFix singleTransReturnResultFix1 = userConfigService.selectResFixByUrid("2");
            List<SingleTransReturnResultFix> singleTransReturnResultFixes1 = userConfigService.selectResFixByUserId("123");
            //返回结果 - 随机
            List<SingleTransReturnResultRandom> singleTransReturnResultRandomListTwo = userConfigService.selectResRandomAll();
            List<SingleTransReturnResultRandom> singleTransReturnResultRandomListTthree = userConfigService.selectResRandomByBankCode("103");
            SingleTransReturnResultRandom singleTransReturnResultRandom = userConfigService.selectResRandomByUrid("1");
            List<SingleTransReturnResultRandom> singleTransReturnResultRandomList = userConfigService.selectResRandomByUserId("123");

            return "";
        }
        catch (Exception ex){
            return ex.getMessage();
        }
    }
}

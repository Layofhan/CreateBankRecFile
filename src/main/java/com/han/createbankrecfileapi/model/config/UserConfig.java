package com.han.createbankrecfileapi.model.config;

import cn.hutool.core.util.ObjectUtil;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultFix;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultRandom;
import lombok.Data;

import java.util.*;

//用户配置信息的统一入口对象
@Data
public class UserConfig {
    /**
     * 用户ID
     */
    private String UserId;
    /**
     * 配置的返回结果-随机的
     */
    private List<SingleTransReturnResultRandom> ReturnResultRandomsLi;
    /**
     * 配置的返回结果-固定的
     */
    private List<SingleTransReturnResultFix> ReturnResultFixesLi;
    /**
     * Ip地址
     */
    private List<String> IpAddressLi;

    /**
     * 银行配置
     * <银行代码，银行配置>
     */
    private Map<String,BankConfig> BankConfigMap;


    //region 重写方法
    public void setReturnResultRandomsLi(SingleTransReturnResultRandom p_SingleTransReturnResultRandom){
        if(this.ReturnResultRandomsLi == null){
            List<SingleTransReturnResultRandom> singleTransReturnResultRandoms = new ArrayList<>();
            singleTransReturnResultRandoms.add(p_SingleTransReturnResultRandom);
            this.ReturnResultRandomsLi = singleTransReturnResultRandoms;
        }
        else {
            this.ReturnResultRandomsLi.add(p_SingleTransReturnResultRandom);
        }
    }
    public void setReturnResultRandomsLi(List<SingleTransReturnResultRandom> p_SingleTransReturnResultRandoms){
        this.ReturnResultRandomsLi = p_SingleTransReturnResultRandoms;
    }

    public void setReturnResultFixesLi(SingleTransReturnResultFix p_SingleTransReturnResultFix){
        if(this.ReturnResultFixesLi == null) {
            List<SingleTransReturnResultFix> singleTransReturnResultFixes = new ArrayList<>();
            singleTransReturnResultFixes.add(p_SingleTransReturnResultFix);
            this.ReturnResultFixesLi = singleTransReturnResultFixes;
        }
        else{
            this.ReturnResultFixesLi.add(p_SingleTransReturnResultFix);
        }
    }
    public void setReturnResultFixesLi(List<SingleTransReturnResultFix> p_SingleTransReturnResultFixs){
        this.ReturnResultFixesLi = p_SingleTransReturnResultFixs;
    }

    public void setBankConfigMap(String p_BankCode, BankConfig p_BankConfig){
        if(ObjectUtil.isNull(this.BankConfigMap)){
            this.BankConfigMap = new HashMap<>();
        }

       this.BankConfigMap.put(p_BankCode,p_BankConfig);
    }
    //endregion

    //region 内部方法

    /**
     * 根据险企号和客户方账号返回固定的配置信息
     * @param p_EnterpriseNum 险企号
     * @param p_CustAccNum 客户方账号
     * @return
     */
    public SingleTransReturnResultFix getFixResultByEnterAndCustAcc(String p_EnterpriseNum,String p_CustAccNum){
        for (SingleTransReturnResultFix singleTransReturnResultFix : this.ReturnResultFixesLi){
            if(singleTransReturnResultFix.getEnterpriseNum().equals(p_EnterpriseNum)  && singleTransReturnResultFix.getCustAccNum().equals(p_CustAccNum)){
                return singleTransReturnResultFix;
            }
        }
        return null;
    }

    /**
     * 随机获取一个返回结果配置
     * @return
     */
    public SingleTransReturnResultRandom getRandomResult(String p_CustBankCode){
        if(this.getReturnResultRandomsLi().size() == 0) {
            return null;
        }
        boolean flag = false;
        for (SingleTransReturnResultRandom randomResult :this.getReturnResultRandomsLi()){
            if(randomResult.getCustBankCode().equals(p_CustBankCode)){
                flag = true;
                break;
            }
        }
        if(!flag){
            return null;
        }
        Random r = new Random();
        //获取一个集合大小内的整数
        Integer randomNum = r.nextInt(this.getReturnResultRandomsLi().size());
        //根据随机生成的整数 获取对应配置
        return  this.getReturnResultRandomsLi().get(randomNum);
    }
    //endregion
}

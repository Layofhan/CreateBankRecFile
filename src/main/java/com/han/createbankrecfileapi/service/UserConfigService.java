package com.han.createbankrecfileapi.service;

import com.han.createbankrecfileapi.dto.*;

import java.util.List;

public interface UserConfigService {

    //region 查找固定的返回结果
    /**
     * 查找所有配置的固定返回结果
     * @return
     */
    List<SingleTransReturnResultFix> selectResFixAll();

    /**
     * 查找配置的返回结果 - 固定
     * @param p_EnterpriseNum 险企号
     * @param p_CustAccNum 客户方银行账号
     * @return
     */
    SingleTransReturnResultFix selectResFixByEnterAndCustAcc(String p_EnterpriseNum, String p_CustAccNum);

    /**
     * 根据用户ID查找返回结果 - 固定
     * @param p_UserId 用户ID
     * @return
     */
    List<SingleTransReturnResultFix> selectResFixByUserId(String p_UserId);

    /**
     * 根据主键查找返回结果 - 固定
     * @param p_Urid 主键
     * @return
     */
    SingleTransReturnResultFix selectResFixByUrid(String p_Urid);
    //endregion

    //region 查找随机的返回结果
    /**
     * 查找所有返回结果 - 随机
     * @return
     */
    List<SingleTransReturnResultRandom> selectResRandomAll();

    /**
     * 根据银行编码查找返回结果 - 随机
     * @param p_BankCode 客户方银行编码
     * @return
     */
    List<SingleTransReturnResultRandom> selectResRandomByBankCode(String p_BankCode);

    /**
     * 根据用户ID查找返回结果 - 随机
     * @param p_UserId 用户ID
     * @return
     */
    List<SingleTransReturnResultRandom> selectResRandomByUserId(String p_UserId);

    /**
     * 根据主键查找返回结果 - 随机
     * @param p_Urid 主键
     * @return
     */
    SingleTransReturnResultRandom selectResRandomByUrid(String p_Urid);
    //endregion

    //region 查找用户信息
    /**
     * 获取所有信息
     * @return
     */
    List<UserInfo> selectUserInfoAll();

    /**
     * 获取对应的用户信息
     * @param p_UserId 用户ID
     * @return
     */
    List<UserInfo> selectUserInfoById(String p_UserId);

    /**
     * 获取对应的用户信息
     * @param p_Urid 主键
     * @return
     */
    UserInfo selectUserInfoByUrid(String p_Urid);
    //endregion

    //region 银行公私钥
    /**
     * 获取所有的数据
     * @return
     */
    List<BankSoftKey> selectBankSoftKeyAll();

    /**
     * 根据用户名查找数据
     * @param p_UserId 用户ID
     * @return
     */
    List<BankSoftKey> selectBankSoftKeyByUserId(String p_UserId);

    /**
     * 根据用户名和银行code查找数据
     * @param p_UserId 用户Id
     * @param p_BankCode 银行代码
     * @return
     */
    BankSoftKey selectBSKByUserIdAndBankCode(String p_UserId,String p_BankCode);
    //endregion

    //region 查找程序自身配置信息

    /**
     * 查找配置信息 理论上来说只有一条
     * @return
     */
    ProgramInfo selectProgramInfo();
    //endregion
}

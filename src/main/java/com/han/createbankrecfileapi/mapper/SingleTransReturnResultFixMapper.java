package com.han.createbankrecfileapi.mapper;

import com.han.createbankrecfileapi.dto.SingleTransReturnResultFix;

import java.util.List;

public interface SingleTransReturnResultFixMapper {
    /**
     * 查找所有
     * @return
     */
    List<SingleTransReturnResultFix> selectAll();

    /**
     * 查找配置的返回结果
     * @param p_EnterpriseNum 险企号
     * @param p_CustAccNum 客户方银行账号
     * @return
     */
    SingleTransReturnResultFix selectResByEnterAndCustAcc(String p_EnterpriseNum,String p_CustAccNum);

    /**
     * 根据用户ID查找返回结果
     * @param p_UserId 用户ID
     * @return
     */
    List<SingleTransReturnResultFix> selectResByUserId(String p_UserId);

    /**
     * 根据主键查找返回结果
     * @param p_Urid 主键
     * @return
     */
    SingleTransReturnResultFix selectResByUrid(String p_Urid);
}

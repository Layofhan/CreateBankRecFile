package com.han.createbankrecfileapi.mapper;

import com.han.createbankrecfileapi.dto.SingleTransReturnResultRandom;

import java.util.List;

public interface SingleTransReturnResultRandomMapper {
    /**
     * 查找所有
     * @return
     */
    List<SingleTransReturnResultRandom> selectAll();

    /**
     * 根据银行编码查找返回结果
     * @param p_BankCode 客户方银行编码
     * @return
     */
    List<SingleTransReturnResultRandom> selectResByBankCode(String p_BankCode);

    /**
     * 根据用户ID查找返回结果
     * @param p_UserId 用户ID
     * @return
     */
    List<SingleTransReturnResultRandom> selectResByUserId(String p_UserId);

    /**
     * 根据主键查找返回结果
     * @param p_Urid 主键
     * @return
     */
    SingleTransReturnResultRandom selectResByUrid(String p_Urid);
}

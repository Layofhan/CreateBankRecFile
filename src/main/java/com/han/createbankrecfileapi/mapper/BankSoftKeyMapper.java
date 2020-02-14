package com.han.createbankrecfileapi.mapper;

import com.han.createbankrecfileapi.dto.BankSoftKey;

import java.util.List;

public interface BankSoftKeyMapper {
    /**
     * 获取所有的数据
     * @return
     */
     List<BankSoftKey> selectAll();

    /**
     * 根据用户名查找数据
     * @param p_UserId 用户ID
     * @return
     */
     List<BankSoftKey> selectByUserId(String p_UserId);

    /**
     * 根据用户名和银行code查找数据
     * @param p_UserId 用户Id
     * @param p_BankCode 银行代码
     * @return
     */
     BankSoftKey selectByUserIdAndBankCode(String p_UserId,String p_BankCode);
}

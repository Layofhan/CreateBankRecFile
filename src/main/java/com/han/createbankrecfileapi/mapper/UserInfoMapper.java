package com.han.createbankrecfileapi.mapper;

import com.han.createbankrecfileapi.dto.UserInfo;

import java.util.List;

public interface UserInfoMapper {
    /**
     * 获取所有信息
     * @return
     */
    List<UserInfo> selectAll();

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
}

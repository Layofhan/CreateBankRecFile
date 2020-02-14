package com.han.createbankrecfileapi.service.imp;

import com.han.createbankrecfileapi.dto.*;
import com.han.createbankrecfileapi.mapper.*;
import com.han.createbankrecfileapi.service.UserConfigService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserConfigServiceImp implements UserConfigService {
    //region 变量
    /**
     * 配置的固定返回结果
     */
    @Autowired
    private SingleTransReturnResultFixMapper singleTransReturnResultFixMapper;
    /**
     * 配置的随机返回结果
     */
    @Autowired
    private SingleTransReturnResultRandomMapper singleTransReturnResultRandomMapper;
    /**
     * 用户信息
     */
    @Autowired
    private UserInfoMapper userInfoMapper;
    /**
     * 银行公私钥
     */
    @Autowired
    private BankSoftKeyMapper bankSoftKeyMapper;
    /**
     * 程序自身配置
     */
    @Autowired
    private ProgramInfoMapper programInfoMapper;
    //endregion

    //region 固定返回结果
    @Override
    public List<SingleTransReturnResultFix> selectResFixAll() {
        return singleTransReturnResultFixMapper.selectAll();
    }

    @Override
    public SingleTransReturnResultFix selectResFixByEnterAndCustAcc(String p_EnterpriseNum, String p_CustAccNum) {
        return singleTransReturnResultFixMapper.selectResByEnterAndCustAcc(p_EnterpriseNum,p_CustAccNum);
    }

    @Override
    public List<SingleTransReturnResultFix> selectResFixByUserId(String p_UserId) {
        return singleTransReturnResultFixMapper.selectResByUserId(p_UserId);
    }

    @Override
    public SingleTransReturnResultFix selectResFixByUrid(String p_Urid) {
        return singleTransReturnResultFixMapper.selectResByUrid(p_Urid);
    }
    //endregion

    //region 随机返回结果
    @Override
    public List<SingleTransReturnResultRandom> selectResRandomAll() {
        return singleTransReturnResultRandomMapper.selectAll();
    }

    @Override
    public List<SingleTransReturnResultRandom> selectResRandomByBankCode(String p_BankCode) {
        return singleTransReturnResultRandomMapper.selectResByBankCode(p_BankCode);
    }

    @Override
    public List<SingleTransReturnResultRandom> selectResRandomByUserId(String p_UserId) {
        return singleTransReturnResultRandomMapper.selectResByUserId(p_UserId);
    }

    @Override
    public SingleTransReturnResultRandom selectResRandomByUrid(String p_Urid) {
        return singleTransReturnResultRandomMapper.selectResByUrid(p_Urid);
    }
    //endregion

    //region 用户信息
    @Override
    public List<UserInfo> selectUserInfoAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserInfo> selectUserInfoById(String p_UserId) {
        return userInfoMapper.selectUserInfoById(p_UserId);
    }

    @Override
    public UserInfo selectUserInfoByUrid(String p_Urid) {
        return userInfoMapper.selectUserInfoByUrid(p_Urid);
    }

    //endregion

    //region 银行公私钥
    @Override
    public List<BankSoftKey> selectBankSoftKeyAll() {
        return bankSoftKeyMapper.selectAll();
    }

    @Override
    public List<BankSoftKey> selectBankSoftKeyByUserId(String p_UserId) {
        return bankSoftKeyMapper.selectByUserId(p_UserId);
    }

    @Override
    public BankSoftKey selectBSKByUserIdAndBankCode(String p_UserId, String p_BankCode) {
        return bankSoftKeyMapper.selectByUserIdAndBankCode(p_UserId,p_BankCode);
    }
    //endregion

    //region 查找程序自身配置信息

    /**
     * 查找配置信息 理论上来说只有一条
     * @return
     */
    @Override
    public ProgramInfo selectProgramInfo(){
        return programInfoMapper.selectAll();
    }
    //endregion
}

package com.han.createbankrecfileapi.common.helper;


import cn.hutool.core.util.ObjectUtil;
import com.han.createbankrecfileapi.Globals;
import com.han.createbankrecfileapi.controller.BootInit;
import com.han.createbankrecfileapi.controller.NettyServer;
import com.han.createbankrecfileapi.dto.*;
import com.han.createbankrecfileapi.model.config.BankConfig;
import com.han.createbankrecfileapi.model.config.UserConfig;
import com.han.createbankrecfileapi.service.UserConfigService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.StyledEditorKit;
import java.util.*;

@Slf4j
public class ConfigHelper {
    //region 变量
    private UserConfigService userConfigService = (UserConfigService) Globals.getBean(UserConfigService.class); //用户配置信息获取的服务
    /**
     * 所有的配置信息将加载保存到这个变量里面
     * <用户名ID，用户配置信息>
     */
    private Map<String,UserConfig> _UserConfigsMap = new HashMap();
    //全局变量
    private static ConfigHelper configHelper;

    //程序自身的配置不多，所以就直接放在这里面，之后要是多了 可以拎出去 另起一个类
    public String Ip;
    public String Port;
    //endregion

    private ConfigHelper(){
        BootInit.pool.execute(()->{
            try{
                log.info("开始加载配置");
                Init();
            }
            catch (Exception ex){
                log.error("配置加载过程出现错误，错误信息:"+ex.getMessage());
            }
        });

    }

    //将所有的配置信息加载到内存中
    private void Init(){
        //加载用户信息
        LoadUserInfo();
        //加载返回结果配置信息
        LoadReturnResultConfig();
        //加载银行公私钥配置信息
        LoadBankSoftKey();
        //加载程序自身配置
        LoadProgramInfo();
    }

    /**
     * 加载返回结果配置信息
     * @param
     * @return
     */
    private boolean LoadReturnResultConfig(){
        try {
            //region 加载指定返回结果的配置

            //获取数据库中的数据
            List<SingleTransReturnResultFix> singleTransReturnResultFixes = userConfigService.selectResFixAll();
            //这边的遍历与下面的可以在性能上进行优化,可以放在一起只需要遍历一遍 -- 目前由于集合操作还不够熟悉
            for(SingleTransReturnResultFix singleTransReturnResultFix : singleTransReturnResultFixes){
                //将相同的用户配置组合在一起
                if(_UserConfigsMap.containsKey(singleTransReturnResultFix.getUserId())){
                    _UserConfigsMap.get(singleTransReturnResultFix.getUserId()).setReturnResultFixesLi(singleTransReturnResultFix);
                }
            }
            //endregion

            //region 加载随机返回结果的配置
            List<SingleTransReturnResultRandom> singleTransReturnResultRandoms = userConfigService.selectResRandomAll();
            //逻辑同上
            for(SingleTransReturnResultRandom singleTransReturnResultRandom : singleTransReturnResultRandoms){
                //将相同的用户配置组合在一起
                if(_UserConfigsMap.containsKey(singleTransReturnResultRandom.getUserId())){
                    _UserConfigsMap.get(singleTransReturnResultRandom.getUserId()).setReturnResultRandomsLi(singleTransReturnResultRandom);
                }
            }
            //endregion

            return true;
        }
        catch (Exception ex){
            log.error("加载返回结果配置出现错误，错误信息：" + ex.getMessage());
            return false;
        }
    }

    /**
     * 加载用户信息
     * @param
     * @return
     */
    private boolean LoadUserInfo(){
        try {
            //从数据库中获取用户信息数据
            List<UserInfo> userInfos = userConfigService.selectUserInfoAll();
            UserConfig userConfig;
            for(UserInfo userInfo : userInfos){
                userConfig = new UserConfig();
                userConfig.setUserId(userInfo.getUserId());//这边暂不考虑数据为空的情况，在数据导入的时候做控制
                userConfig.setIpAddressLi(Arrays.asList(userInfo.getIpAddress().split(",")));//同理数据格式暂不校验

                _UserConfigsMap.put(userInfo.getUserId(),userConfig); //暂时不考虑用户名重复问题,这个在数据输入的时候就做控制
            }

            return true;
        }catch (Exception ex){
            log.error("加载返回结果配置出现错误，错误信息：" + ex.getMessage());
            return false;
        }
    }

    /**
     * 加载银行公私钥信息
     * @return
     */
    private boolean LoadBankSoftKey(){
        try {
            //获取数据
            List<BankSoftKey> bankSoftKeys = userConfigService.selectBankSoftKeyAll();
            if(ObjectUtil.isNull(bankSoftKeys)){
                log.info("不存在银行公私钥的配置");
                return true;
            }
            for(BankSoftKey bankSoftKey : bankSoftKeys){
                if(_UserConfigsMap.containsKey(bankSoftKey.getUserId())){
                    //将数据库对象转化为model对象
                    String[] publicKey = bankSoftKey.getPublicKey().split(",");
                    String[] privateKey = bankSoftKey.getPrivateKey().split(",");
                    BankConfig bankConfig = new BankConfig();
                    bankConfig.setPublicKeyModulus(publicKey[0]);
                    bankConfig.setPublicKeyExponent(publicKey[1]);
                    bankConfig.setPrivateKeyModulus(privateKey[0]);
                    bankConfig.setPrivateKeyExponent(privateKey[1]);
                    bankConfig.setD(bankSoftKey.getD());
                    bankConfig.setP(bankSoftKey.getP());
                    bankConfig.setQ(bankSoftKey.getQ());
                    bankConfig.setDP(bankSoftKey.getDP());
                    bankConfig.setDQ(bankSoftKey.getDQ());
                    bankConfig.setInverseQ(bankSoftKey.getInverseQ());
                    _UserConfigsMap.get(bankSoftKey.getUserId()).setBankConfigMap(bankSoftKey.getBankCode(),bankConfig);
                }
            }

            return true;
        }catch (Exception ex){
            log.error("加载银行公私钥配置出现错误，错误信息：" + ex.getMessage());
            return false;
        }
    }

    /**
     * 加载程序自身配置
     * @return
     */
    private boolean LoadProgramInfo(){
        try {
            //获取数据
            ProgramInfo bankSoftKeys = userConfigService.selectProgramInfo();
            if(ObjectUtil.isNull(bankSoftKeys)){
                log.info("程序信息没有配置");
                return true;
            }
            this.Ip = bankSoftKeys.getIp();
            this.Port = bankSoftKeys.getPort();

            return true;
        }catch (Exception ex){
            log.error("加载程序自身配置出现错误，错误信息：" + ex.getMessage());
            return false;
        }
    }
    /**
     * 获取配置类的唯一实例
     * @return
     */
    public static ConfigHelper GetInstance(){
        if (configHelper == null)
        {
            synchronized (NettyServer.class) {
                if (configHelper == null) {
                    configHelper = new ConfigHelper();
                }
            }
        }
        return configHelper;
    }

    /**
     * 获取当前的配置文件
     * @return
     */
    public UserConfig GetUserConfigsByIp(String p_IpAddress) throws Exception {
        UserConfig userConfig = null;
        for (String userId : _UserConfigsMap.keySet()){
            if(_UserConfigsMap.get(userId).getIpAddressLi().contains(p_IpAddress)){
                userConfig = _UserConfigsMap.get(userId);
                break;
            }
        }
        if(ObjectUtil.isNull(userConfig)){
            throw new Exception("获取不到IP地址为：" + p_IpAddress + "的配置信息，请检查");
        }
        return userConfig;
    }
}

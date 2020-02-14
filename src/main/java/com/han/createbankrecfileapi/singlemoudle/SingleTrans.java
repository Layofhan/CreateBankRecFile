package com.han.createbankrecfileapi.singlemoudle;

import com.han.createbankrecfileapi.model.XmlObject;
import com.han.createbankrecfileapi.model.config.UserConfig;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransHead;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
@Slf4j
public class SingleTrans {
    //region 全局变量
    StringBuilder returnXml = new StringBuilder();//返回报文变量
    private UserConfig _UserConfig = null;
    //endregion

    /**
     * 单笔交易 这里来处理及分发单笔交易下的各个类型的交易
     * @param p_Xml 请求报文
     * @return 返回报文
     */
    public String deal(String p_Xml){
        XmlObject xmlObject = new XmlObject();
        RealTimeSingleTransHead singleTransHead = new RealTimeSingleTransHead();
        RealTimeSingleTransReq singleTransReq = new RealTimeSingleTransReq();
        //解析报文 转化为对象
        StringBuilder errorMessage = new StringBuilder();//错误信息
        this.parseXml(p_Xml,xmlObject,errorMessage);
        if(errorMessage.length() != 0){
            //错误处理
            return errorDeal(xmlObject,errorMessage,"E8001","-1");
        }
        //校验数据是否正确
        this.checkData(xmlObject,errorMessage);
        if(errorMessage.length() != 0) {
            //错误处理
            return errorDeal(xmlObject, errorMessage, "E8001", "-1");
        }
        //验签
        this.verify(xmlObject,errorMessage);
        if(errorMessage.length() != 0){
            //错误处理
            return errorDeal(xmlObject,errorMessage,"E8001","-1");
        }
        //签名
        this.sign(xmlObject,errorMessage);
        if(errorMessage.length() != 0){
            //错误处理
            return errorDeal(xmlObject,errorMessage,"E8001","-1");
        }
        //生成交易结果，一般的特殊化处理都在这个步骤里面做,根据不同的情况返回不同的结果
        this.createResult(xmlObject,errorMessage);
        if(errorMessage.length() != 0){
            return errorDeal(xmlObject,errorMessage,"E8001","-1");
        }
        //返回报文的生成
        this.createXml(xmlObject,errorMessage);
        if(errorMessage.length() != 0){
            return errorDeal(xmlObject,errorMessage,"E8001","-1");
        }

        return  ""+returnXml;
    }

    /**
     * 解析传入的xml报文
     * @return
     */
    void parseXml(String p_Xml,XmlObject p_XmlObject,StringBuilder p_ErrorMsg){

    }

    /**
     * 校验报文中的数据
     * @param p_XmlObject
     * @return
     */
    void checkData(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
    }

    /**
     * 验签
     * @param p_XmlObject
     * @param p_ErrorMsg
     */
    void verify(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
    }

    /**
     * 签名
     * @param p_XmlObject
     * @param p_ErrorMsg
     */
    void sign(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
    }

    /**
     * 生成返回结果 包括：交易信息 信息码 交易状态
     * @param p_XmlObject 请求报文对象
     * @return
     */
    void createResult(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
    }

    /**
     * 错误处理类
     * @return
     */
    String errorDeal(XmlObject p_XmlObject,StringBuilder p_ErrorMsg,String p_InfoCode,String p_State){
        ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfo(p_ErrorMsg.toString());//赋值详细信息
        ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfoCode(p_InfoCode);//赋值信息码
        ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setState(p_State);//赋值状态
        p_ErrorMsg.delete(0,p_ErrorMsg.length());//上面赋值之后，可以清空 继续使用这个对象
        return this.createFailXml(p_XmlObject,p_ErrorMsg);
    }

    /**
     * 生成错误返回报文
     * @param p_XmlObject 请求报文对象
     * @return 返回报文
     */
    String createFailXml(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        try {
            //生成报文
            this.createXml(p_XmlObject,p_ErrorMsg);
            if(p_ErrorMsg.length() != 0){
                return "<Root><Head><RespCode>-1</RespCode><RespInfo>模拟工具生成返回报文异常,错误信息：" + p_ErrorMsg.toString() + "</RespInfo></Head></Root>";
            }
            return ""+returnXml;
        }
        catch (Exception ex){
            return "<Root><Head><RespCode>-1</RespCode><RespInfo>模拟工具生成返回报文异常," + ex.getMessage() + "</RespInfo></Head></Root>";
        }
    }

    /**
     * 生成返回报文
     * @param p_XmlObject 请求报文转化后的对象
     * @return 错误信息
     */
    void createXml(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
    }

    void createElement(Document document, Element targetElement, String key, String value) {
        Element sourceElement = document.createElement(key);
        sourceElement.setTextContent(value);
        targetElement.appendChild(sourceElement);
    }

    /**
     * 是否需要进行验签判断
     * @param p_Version 版本号
     * @return true - 需要，false - 不需要
     */
    Boolean needVerify(String p_Version){
        //1.0版本以外的都不要进行验签
        if(p_Version.equals("1.0")){
            return true;
        }
        return false;
    }

    //region 方法 杂 存放无关逻辑的方法
    public void setUserConfig(UserConfig p_UserConfig){
        this._UserConfig = p_UserConfig;
    }
    public UserConfig getUserConfig(){
        return this._UserConfig;
    }
    //endregion

//region 查询数据库代码demo
//        List<Department> list = departmentService.selectAll();
//        StringBuilder stringBuilder = new StringBuilder();
//        for(Department li : list){
//            stringBuilder.append(li.getId() + li.getName() + "\r\n");
//        }
//        return stringBuilder.toString();
    //endregion
}

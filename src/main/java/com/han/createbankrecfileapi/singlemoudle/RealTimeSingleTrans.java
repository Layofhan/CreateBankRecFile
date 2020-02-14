package com.han.createbankrecfileapi.singlemoudle;

import cn.hutool.core.util.ObjectUtil;
import com.han.createbankrecfileapi.common.createXml.XmlUntil;
import com.han.createbankrecfileapi.common.helper.ConfigHelper;
import com.han.createbankrecfileapi.common.helper.DataException;
import com.han.createbankrecfileapi.common.helper.UniversalUtils;
import com.han.createbankrecfileapi.common.helper.VerifyHelper;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultFix;
import com.han.createbankrecfileapi.dto.SingleTransReturnResultRandom;
import com.han.createbankrecfileapi.model.Head;
import com.han.createbankrecfileapi.model.XmlObject;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransHead;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransReq;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeTransDetial;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Map;

@Component
@Slf4j
public class RealTimeSingleTrans extends SingleTrans {

    //region 全局变量
    RealTimeSingleTransHead singleTransHead;//报文头部信息
    RealTimeSingleTransReq singleTransReq;//报文主体信息
    RealTimeTransDetial transDetial;//报文明细
    String SignData;//签名数据
    //endregion

    /**
     * 解析传入的xml报文
     * @return
     */
    @Override
    void parseXml(String p_Xml, XmlObject p_XmlObject, StringBuilder p_ErrorMsg){ //SingleTransHead p_SingleTransHead, SingleTransReq p_SingleTransReq
        try {
            log.info("开始解析传入报文");

            //region 声明对象
            singleTransHead = new RealTimeSingleTransHead();
            singleTransReq = new RealTimeSingleTransReq();
            transDetial = new RealTimeTransDetial();
            Map<String, Object> ato = null;
            //endregion

            // 解析报文
            ato = XmlUntil.xml2map(p_Xml, false);
            Map<String, Object> headReq = (Map<String, Object>) ato.get("Head");
            Map<String, Object> realTimeSingleTransReq = (Map<String, Object>) ato.get("RealTimeSingleTransReq");
            Map<String, Object> transReq = (Map<String, Object>) realTimeSingleTransReq.get("Trans");
            // 对象赋值
            singleTransHead = analyzeHead(headReq);//头部信息
            transDetial = analyzeTrans(transReq);//明细部分
            singleTransReq = analyzeRealTimeSingleTransReq(realTimeSingleTransReq, transDetial);//详细信息

            p_XmlObject.setHead((Head) singleTransHead);
            p_XmlObject.setReq(singleTransReq);

            log.info("报文解析成功");
        }
        catch (Exception ex){
            log.error("单笔报文解析出错,出错信息:"+ex.getMessage()+",错误位置:"+ex.getStackTrace());
            p_ErrorMsg.append("单笔报文解析出错");
        }
    }

    /**
     * 校验报文中的数据
     * @param p_XmlObject
     * @return
     */
    @Override
    void checkData(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        try {
            log.info("开始校验数据");
            //head
            UniversalUtils.checkStringParam("EnterpriseNum", p_XmlObject.getHead().getEnterpriseNum(), 10, true);
            UniversalUtils.checkStringParam("Version", p_XmlObject.getHead().getVersion(), 10, false);
            UniversalUtils.checkStringParam("CommandCode", p_XmlObject.getHead().getCommandCode(), 5, true);
            UniversalUtils.checkStringParam("TransSeqID", p_XmlObject.getHead().getTransSeqID(), 20, false);
            UniversalUtils.checkStringParam("VerifyCode", p_XmlObject.getHead().getVerifyCode(), 180, false);
            UniversalUtils.checkStringParam("ZipType", p_XmlObject.getHead().getZipType(), 1, false);
            //transReq
            UniversalUtils.checkStringParam("MoneyWay", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getMoneyWay(), 1, true);
            UniversalUtils.checkStringParam("TransDate", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTransDate(), 8, true);
            UniversalUtils.checkStringParam("AccountingFlag", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getAccountingFlag(), 1, true);
            //trans
            UniversalUtils.checkStringParam("TransNo", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getTransNo(), 50, true);
            UniversalUtils.checkStringParam("EnterpriseAccNum", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getEnterpriseAccNum(), 32, true);
            UniversalUtils.checkStringParam("CustBankCode", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustBankCode(), 8, false);
            UniversalUtils.checkStringParam("CustAccNum", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustAccNum(), 32, true);
            UniversalUtils.checkStringParam("CustAccName", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustAccName(), 128, true);
            UniversalUtils.checkStringParam("AreaCode", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getAreaCode(), 8, false);
            UniversalUtils.checkStringParam("BankLocationCode", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getBankLocationCode(), 12, false);
            UniversalUtils.checkStringParam("BankLocationName", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getBankLocationName(), 64, false);
            UniversalUtils.checkStringParam("CardType", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCardType(), 1, false);
            UniversalUtils.checkStringParam("IsPrivate", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getIsPrivate(), 1, false);
            UniversalUtils.checkStringParam("IsUrgent", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getIsUrgent(), 1, false);
            UniversalUtils.checkStringParam("Amount", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getAmount(), 18, true);
            UniversalUtils.checkStringParam("Currency", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCurrency(), 3, false);
            UniversalUtils.checkStringParam("CertType", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCertType(), 1, false);
            UniversalUtils.checkStringParam("CertNum", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCertNum(), 32, false);
            UniversalUtils.checkStringParam("Mobile", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getMobile(), 11, false);
            UniversalUtils.checkStringParam("Purpose", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getPurpose(), 64, false);
            UniversalUtils.checkStringParam("Memo", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getMemo(), 64, false);
            UniversalUtils.checkStringParam("ReconciliationCode", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getReconciliationCode(), 64, false);
            UniversalUtils.checkStringParam("PolicyNumber", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getPolicyNumber(), 30, false);
            UniversalUtils.checkStringParam("Extent1", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getExtent1(), 100, false);
            UniversalUtils.checkStringParam("Extent2", ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getExtent2(), 100, false);

            log.info("数据校验成功");
        }
        catch (DataException ex){
            log.error("收到的报文参数格式不正确，错误信息:"+ex.getMessage());
            p_ErrorMsg.append("收到的报文参数格式不正确，错误信息:"+ex.getMessage());
        }
        catch (Exception ex){
            log.error("单笔数据校验出错,出错信息:"+ex.getMessage()+",错误位置:"+ex.getStackTrace());
            p_ErrorMsg.append("单笔数据校验出错");
        }
    }

    /**
     * 验签
     * @param p_XmlObject
     * @param p_ErrorMsg
     */
    @Override
    void verify(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        try {
            String transNo = transDetial.getTransNo();
            log.info("流水号:"+transNo+"开始进行验签操作");
            //获取银行配置
            String custBankCode = transDetial.getCustBankCode();
            if(!super.getUserConfig().getBankConfigMap().containsKey(custBankCode)){
                //没有则直接进行返回
                p_ErrorMsg.append("模拟工具没有配置银行代码为：" + custBankCode + "的公私钥，请联系管理员");
                return;
            }
            String version = singleTransHead.getVersion();
            String enterpriseNum = singleTransHead.getEnterpriseNum();
            String commandCode = singleTransHead.getFGCommandCode();
            String moneyWay = singleTransReq.getMoneyWay();
            String enterpriseAccNum = transDetial.getEnterpriseAccNum();
            String custAccName = transDetial.getCustAccNum();
            String custAccNum = transDetial.getCustAccNum();
            String amount = transDetial.getAmount();
            //原始验签码
            String originalVerifyCode = singleTransHead.getVerifyCode();
            log.info("原始验签码为：" + originalVerifyCode);
            //准备验签的数据
            if(version.equals("1.1")){
                SignData = enterpriseNum + "_" + transNo + "_" + custAccNum + "_" + amount;
            } else {
                SignData = enterpriseNum + "_" +commandCode + "_" + moneyWay + "_" + transNo + "_" + enterpriseAccNum + "_" + custAccNum + "_" + custAccName + "_" + amount;
            }
            log.info("验签数据为：" + SignData);
            //放在这里判断是为了准备数据，之后签名需要用到这些数据
            //签名是肯定执行的
            if(!needVerify(p_XmlObject.getHead().getVersion())){
                log.info("不需要验签");
                return;
            }
            //获取验签数据的MD5
            String verifyMD5 = VerifyHelper.getMD5(SignData);
            log.info("MD5为：" + verifyMD5);
            //校验
            Boolean verifyFlag= VerifyHelper.verifyData(verifyMD5,originalVerifyCode,super.getUserConfig().getBankConfigMap().get(custBankCode));
            if(!verifyFlag){
                log.info("流水号:"+transNo+"数据校验失败");
                p_ErrorMsg.append("数据验签失败，请校验数据");
            }
        }catch (Exception ex){
            log.error("数据验签出错,出错信息:"+ex.getMessage()+",错误位置:"+ex.getStackTrace());
            p_ErrorMsg.append("单笔数据校验出错");
        }
    }

    @Override
    void sign(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        try {
            log.info("开始进行签名，签名数据为：" + SignData);
            //获取验签数据的MD5
            String verifyMD5 = VerifyHelper.getMD5(SignData);
            log.info("MD5为：" + verifyMD5);
            //签名
            String signData = VerifyHelper.sign(verifyMD5
                                    ,super.getUserConfig().getBankConfigMap().get(((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustBankCode()));
            log.info("签名后的数据为:" + signData);
            p_XmlObject.getHead().setVerifyCode(signData);
        }catch (Exception ex){
            log.error("单笔数据签名出现错误,出错信息:"+ex.getMessage()+",错误位置:"+ex.getStackTrace());
            p_ErrorMsg.append("单笔数据签名出现错误");
        }
    }

    /**
     * 生成返回结果 包括：交易信息 信息码 交易状态
     * @param p_XmlObject 请求报文对象
     * @return
     */
    @Override
    void createResult(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        try {
            log.info("开始生成返回结果");
            //判断有没有固定返回结果配置
            SingleTransReturnResultFix singleTransReturnResultFix = super.getUserConfig().getFixResultByEnterAndCustAcc(p_XmlObject.getHead().getEnterpriseNum()
                    , ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustAccNum());
            //返回true则说明存在固定结果的配置信息
            if(!ObjectUtil.isNull(singleTransReturnResultFix)){
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfo(singleTransReturnResultFix.getInfo());//赋值详细信息
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfoCode(singleTransReturnResultFix.getCode());//赋值信息码
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setState(singleTransReturnResultFix.getState());//赋值状态
                log.info("批次号:"+p_XmlObject.getHead().getTransSeqID()+"返回结果生成成功，返回码为："+singleTransReturnResultFix.getCode()+"，返回信息为："+singleTransReturnResultFix.getInfo()+"，返回状态为："+singleTransReturnResultFix.getState());
                return;
            }
            log.info("批次号:"+p_XmlObject.getHead().getTransSeqID()+"没有配置固定返回结果");
            //没有固定的则随机进行返回
            SingleTransReturnResultRandom singleTransReturnResultRandom = super.getUserConfig().getRandomResult(((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().getCustBankCode());
            if(!ObjectUtil.isNull(singleTransReturnResultRandom)){
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfo(singleTransReturnResultRandom.getInfo());//赋值详细信息
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfoCode(singleTransReturnResultRandom.getCode());//赋值信息码
                ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setState(singleTransReturnResultRandom.getState());//赋值状态
                log.info("批次号:"+p_XmlObject.getHead().getTransSeqID()+"返回结果生成成功，返回码为："+singleTransReturnResultRandom.getCode()+"，返回信息为："+singleTransReturnResultRandom.getInfo()+"，返回状态为："+singleTransReturnResultRandom.getState());
                return;
            }
            log.info("批次号:"+p_XmlObject.getHead().getTransSeqID()+"没有配置随机返回结果");
            //如果连随机都没有的 就直接返回成功
            ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfo("交易成功");//赋值详细信息
            ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setInfoCode("S0000");//赋值信息码
            ((RealTimeSingleTransReq)p_XmlObject.getReq()).getTrans().setState("3");//赋值状态
            log.info("批次号:"+p_XmlObject.getHead().getTransSeqID()+"返回结果生成成功，返回码为：S0000，返回信息为：交易成功，返回状态为：3");
        }
        catch (Exception ex){
            log.error("单笔数据校验出错,出错信息:"+ex.getMessage()+",错误位置:"+ex.getStackTrace());
            p_ErrorMsg.append("单笔数据校验出错");
        }
    }

    /**
     * 生成返回报文
     * @param p_XmlObject 请求报文转化后的对象
     * @return 错误信息
     */
    @Override
    void createXml(XmlObject p_XmlObject,StringBuilder p_ErrorMsg){
        log.info("开始生成返回报文");
        Head headObj = p_XmlObject.getHead();
        RealTimeSingleTransReq transReqObj = (RealTimeSingleTransReq) p_XmlObject.getReq();
        RealTimeTransDetial transDetialObj = transReqObj.getTrans();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);

            Element root = document.createElement("Root");
            // 头节点
            Element head = document.createElement("Head");
            //明细节点
            Element realTimeSingleTransReq = document.createElement("RealTimeSingleTransResq");
            //明细
            Element trans = document.createElement("Trans");
            // 头节点赋值
            createElement(document, head, "CommandCode", headObj.getCommandCode());
            createElement(document, head, "TransSeqID", headObj.getTransSeqID());
            createElement(document, head, "VerifyCode", headObj.getVerifyCode());
            createElement(document, head, "ZipType", headObj.getZipType());
            createElement(document, head, "CorpBankCode", headObj.getCorpBankCode());
            createElement(document, head, "FGCommandCode", headObj.getFGCommandCode());
            createElement(document, head, "EnterpriseNum", headObj.getEnterpriseNum());
            createElement(document, head, "Version", headObj.getVersion());
            createElement(document, head, "RespCode", headObj.getRespCode());
            createElement(document, head, "RespInfo", headObj.getRespInfo());
            //明细节点赋值
            createElement(document, realTimeSingleTransReq, "MoneyWay", transReqObj.getMoneyWay());
            createElement(document, realTimeSingleTransReq, "TransDate", transReqObj.getTransDate());
            createElement(document, realTimeSingleTransReq, "BusType", transReqObj.getBusType());
            createElement(document, realTimeSingleTransReq, "AccountingFlag", transReqObj.getAccountingFlag());
            //明细赋值
            createElement(document, trans, "TransNo", transDetialObj.getTransNo());
            createElement(document, trans, "ProtocolCode", transDetialObj.getProtocolCode());
            createElement(document, trans, "EnterpriseAccNum", transDetialObj.getEnterpriseAccNum());
            createElement(document, trans, "CustBankCode", transDetialObj.getCustBankCode());
            createElement(document, trans, "CustAccNum", transDetialObj.getCustAccNum());
            createElement(document, trans, "CustAccName", transDetialObj.getCustAccName());
            createElement(document, trans, "State", transDetialObj.getState());
            createElement(document, trans, "InfoCode", transDetialObj.getInfoCode());
            createElement(document, trans, "Info", transDetialObj.getInfo());
            createElement(document, trans, "AreaCode", transDetialObj.getAreaCode());
            createElement(document, trans, "BankLocationCode", transDetialObj.getBankLocationCode());
            createElement(document, trans, "BankLocationName", transDetialObj.getBankLocationName());
            createElement(document, trans, "CardType", transDetialObj.getCardType());
            createElement(document, trans, "IsPrivate", transDetialObj.getIsPrivate());
            createElement(document, trans, "IsUrgent", transDetialObj.getIsUrgent());
            createElement(document, trans, "Amount", transDetialObj.getAmount());
            createElement(document, trans, "Currency", transDetialObj.getCurrency());
            createElement(document, trans, "CertType", transDetialObj.getCertType());
            createElement(document, trans, "CertNum", transDetialObj.getCertNum());
            createElement(document, trans, "Mobile", transDetialObj.getMobile());
            createElement(document, trans, "Purpose", transDetialObj.getMemo());
            createElement(document, trans, "PolicyNumber", transDetialObj.getPolicyNumber());
            createElement(document, trans, "Extent1", transDetialObj.getExtent1());
            createElement(document, trans, "Extent2", transDetialObj.getExtent2());
            createElement(document, trans, "SourceTransNo", transDetialObj.getSourceTransNo());

            //head节点
            root.appendChild(head);
            //realTimeSingleTransReq节点
            realTimeSingleTransReq.appendChild(trans);
            root.appendChild(realTimeSingleTransReq);
            document.appendChild(root);

            //将xml对象转化为字符串
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            //这边将生成的报文进行保存操作
            returnXml = new StringBuilder(writer.getBuffer().toString());
            //保存报文
            //saveReturnXml(headObj.getEnterpriseNum(),transObj.getTransNo(),returnXml);
            log.info("返回报文生成成功");
        }
        catch (Exception ex){
            log.error("生成返回报文出错，错误信息为：" +ex.getMessage() + "错误位置为："+ex.getStackTrace());
            p_ErrorMsg.append("生成返回报文出错");
        }
    }

    /**
     * 将map类转化为Head对象
     * @param headReq
     * @return
     */
    private RealTimeSingleTransHead analyzeHead(Map<String, Object> headReq) {
        RealTimeSingleTransHead head = new RealTimeSingleTransHead();
        head.setCommandCode(UniversalUtils.objToStrUtil(headReq.get("CommandCode")));
        head.setTransSeqID(UniversalUtils.objToStrUtil(headReq.get("TransSeqID")));
        head.setVerifyCode(UniversalUtils.objToStrUtil(headReq.get("VerifyCode")));
        head.setZipType(UniversalUtils.objToStrUtil(headReq.get("ZipType")));
        head.setCorpBankCode(UniversalUtils.objToStrUtil(headReq.get("CorpBankCode")));
        head.setFGCommandCode(UniversalUtils.objToStrUtil(headReq.get("FGCommandCode")));
        head.setEnterpriseNum(UniversalUtils.objToStrUtil(headReq.get("EnterpriseNum")));
        head.setTransKeyEncryptFlag(UniversalUtils.objToStrUtil(headReq.get("TransKeyEncryptFlag")));
        head.setRespCode(UniversalUtils.objToStrUtil(headReq.get("RespCode")));
        head.setRespInfo(UniversalUtils.objToStrUtil(headReq.get("RespInfo")));
        head.setVersion(UniversalUtils.objToStrUtil(headReq.get("Version")));
        return head;
    }

    /**
     * 将map类转化为Req对象
     * @param transReq
     * @return
     */
    private RealTimeTransDetial analyzeTrans(Map<String, Object> transReq) {
        RealTimeTransDetial transDetial = new RealTimeTransDetial();
        transDetial.setTransNo(UniversalUtils.objToStrUtil(transReq.get("TransNo")));
        transDetial.setProtocolCode(UniversalUtils.objToStrUtil(transReq.get("ProtocolCode")));
        transDetial.setEnterpriseAccNum(UniversalUtils.objToStrUtil(transReq.get("EnterpriseAccNum")));
        transDetial.setCustBankCode(UniversalUtils.objToStrUtil(transReq.get("CustBankCode")));
        transDetial.setCustAccNum(UniversalUtils.objToStrUtil(transReq.get("CustAccNum")));
        transDetial.setCustAccName(UniversalUtils.objToStrUtil(transReq.get("CustAccName")));
        transDetial.setAreaCode(UniversalUtils.objToStrUtil(transReq.get("AreaCode")));
        transDetial.setBankLocationCode(UniversalUtils.objToStrUtil(transReq.get("BankLocationCode")));
        transDetial.setBankLocationName(UniversalUtils.objToStrUtil(transReq.get("BankLocationName")));
        transDetial.setCardType(UniversalUtils.objToStrUtil(transReq.get("CardType")));
        transDetial.setIsPrivate(UniversalUtils.objToStrUtil(transReq.get("IsPrivate")));
        transDetial.setIsUrgent(UniversalUtils.objToStrUtil(transReq.get("IsUrgent")));
        transDetial.setAmount(String.format("%1$.2f",UniversalUtils.objToFloatUtil(transReq.get("Amount"))/100f));
        transDetial.setCurrency(UniversalUtils.objToStrUtil(transReq.get("Currency")));
        transDetial.setCertType(UniversalUtils.objToStrUtil(transReq.get("CertType")));
        transDetial.setCertNum(UniversalUtils.objToStrUtil(transReq.get("CertNum")));
        transDetial.setMobile(UniversalUtils.objToStrUtil(transReq.get("Mobile")));
        transDetial.setPurpose(UniversalUtils.objToStrUtil(transReq.get("Purpose")));
        transDetial.setMemo(UniversalUtils.objToStrUtil(transReq.get("Memo")));
        transDetial.setReconciliationCode(UniversalUtils.objToStrUtil(transReq.get("ReconciliationCode")));
        transDetial.setPolicyNumber(UniversalUtils.objToStrUtil(transReq.get("PolicyNumber")));
        transDetial.setExtent1(UniversalUtils.objToStrUtil(transReq.get("Extent1")));
        transDetial.setExtent2(UniversalUtils.objToStrUtil(transReq.get("Extent2")));
        transDetial.setSourceTransNo(UniversalUtils.objToStrUtil(transReq.get("SourceTransNo")));
        return transDetial;
    }

    /**
     * 将map类转化为Trans对象
     * @param realTimeSingleTransReq
     * @param p_TransDetial
     * @return
     */
    private RealTimeSingleTransReq analyzeRealTimeSingleTransReq(Map<String, Object> realTimeSingleTransReq, RealTimeTransDetial p_TransDetial) {
        RealTimeSingleTransReq singleTransReq = new RealTimeSingleTransReq();
        singleTransReq.setMoneyWay(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("MoneyWay")));
        singleTransReq.setTransDate(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("TransDate")));
        singleTransReq.setTrans(p_TransDetial);
        singleTransReq.setBusType(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("BusType")));
//        rtstr.setAgentNo(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("AgentNo")));
//        rtstr.setAgentType(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("AgentType")));
        singleTransReq.setAccountingFlag(UniversalUtils.objToStrUtil(realTimeSingleTransReq.get("AccountingFlag")));
        return singleTransReq;
    }
}

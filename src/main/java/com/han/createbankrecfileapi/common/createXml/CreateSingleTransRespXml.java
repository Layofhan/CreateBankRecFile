package com.han.createbankrecfileapi.common.createXml;

import com.han.createbankrecfileapi.model.XmlObject;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransHead;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeSingleTransReq;
import com.han.createbankrecfileapi.model.realtimedeal.RealTimeTransDetial;
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

public class CreateSingleTransRespXml extends XmlUntil {

    @Override
    public String Create(XmlObject xmlObject) {
        RealTimeSingleTransHead headObj = (RealTimeSingleTransHead)xmlObject.getHead();
        RealTimeSingleTransReq reqObj = (RealTimeSingleTransReq)xmlObject.getReq();
        RealTimeTransDetial transObj = reqObj.getTrans();
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
            //region 头节点赋值
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
            //endregion

            //region明细节点赋值
            createElement(document, realTimeSingleTransReq, "MoneyWay", reqObj.getMoneyWay());
            createElement(document, realTimeSingleTransReq, "TransDate", reqObj.getTransDate());
            createElement(document, realTimeSingleTransReq, "BusType", reqObj.getBusType());
            createElement(document, realTimeSingleTransReq, "AccountingFlag", reqObj.getAccountingFlag());
            //endregion

            //region 明细赋值
            createElement(document, trans, "TransNo", transObj.getTransNo());
            createElement(document, trans, "ProtocolCode", transObj.getProtocolCode());
            createElement(document, trans, "EnterpriseAccNum", transObj.getEnterpriseAccNum());
            createElement(document, trans, "CustBankCode", transObj.getCustBankCode());
            createElement(document, trans, "CustAccNum", transObj.getCustAccNum());
            createElement(document, trans, "CustAccName", transObj.getCustAccName());
            createElement(document, trans, "State", transObj.getState());
            createElement(document, trans, "InfoCode", transObj.getInfoCode());
            createElement(document, trans, "Info", transObj.getInfo());
            createElement(document, trans, "AreaCode", transObj.getAreaCode());
            createElement(document, trans, "BankLocationCode", transObj.getBankLocationCode());
            createElement(document, trans, "BankLocationName", transObj.getBankLocationName());
            createElement(document, trans, "CardType", transObj.getCardType());
            createElement(document, trans, "IsPrivate", transObj.getIsPrivate());
            createElement(document, trans, "IsUrgent", transObj.getIsUrgent());
            createElement(document, trans, "Amount", transObj.getAmount());
            createElement(document, trans, "Currency", transObj.getCurrency());
            createElement(document, trans, "CertType", transObj.getCertType());
            createElement(document, trans, "CertNum", transObj.getCertNum());
            createElement(document, trans, "Mobile", transObj.getMobile());
            createElement(document, trans, "Purpose", transObj.getPurpose());
            createElement(document, trans, "Memo", transObj.getMemo());
            createElement(document, trans, "PolicyNumber", transObj.getPolicyNumber());
            createElement(document, trans, "Extent1", transObj.getExtent1());
            createElement(document, trans, "Extent2", transObj.getExtent2());
            //endregion

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
            String returnXml = writer.getBuffer().toString();
            super.saveReturnXml(headObj.getEnterpriseNum(),transObj.getTransNo(),returnXml);
            return returnXml;

        } catch (Exception e) {
            return  "<Root><Head><RespCode>-1</RespCode><RespInfo>生成返回报文错误</RespInfo></Head></Root>";
        }
    }
    //添加节点
    private static void createElement(Document document, Element targetElement, String key, String value) {
        Element sourceElement = document.createElement(key);
        sourceElement.setTextContent(value);
        targetElement.appendChild(sourceElement);
    }
}

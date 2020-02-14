package com.han.createbankrecfileapi.common.createXml;

import com.han.createbankrecfileapi.model.XmlObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.*;

public class XmlUntil {

    //保存报文
    void saveReturnXml(String p_EnterpriseNum,String p_FlowNo,String p_Xml) throws Exception{

    }
    //生成报文
    public String Create(XmlObject xmlObject){
        return null;
    }
    //生成失败报文
    public String CreateFailXml(XmlObject xmlObject,String errorMsg,String errorInfoCode,String errorInfo){

        return this.Create(xmlObject);
    }
    //解析xml
    public XmlObject AnalysisXml(String p_Xml){
        return null;
    }

    /**
     * xml转map 不带属性
     *
     * @param xmlStr
     * @param needRootKey
     *            是否需要在返回的map里加根节点键
     * @return
     * @throws DocumentException
     * @throws SAXException
     */
    public static Map<String, Object> xml2map(String xmlStr, boolean needRootKey) throws DocumentException, SAXException {
        Document doc = parseText(xmlStr);
        Element root = doc.getRootElement();
        Map<String, Object> map = (Map<String, Object>) xml2map(root);
        if (root.elements().size() == 0 && root.attributes().size() == 0) {
            return map;
        }
        if (needRootKey) {
            // 在返回的map里加根节点键（如果需要）
            Map<String, Object> rootMap = new HashMap<String, Object>();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }

    /**
     * xml转map 不带属性
     *
     * @param e
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> xml2map(Element e) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        List<Element> list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = list.get(i);
                List<Object> mapList = new ArrayList<Object>();

                if (iter.elements().size() > 0) {
                    Map<String, Object> m = xml2map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj instanceof List) {
                            mapList = (List<Object>) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj instanceof List) {
                            mapList = (List<Object>) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else{
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else{
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    /**
     * 解析document（禁用DTDs）
     *
     * @param text
     *            xml字符串
     * @return Document对象
     * @throws DocumentException
     *             DocumentException
     * @throws SAXException
     *             SAXException
     * @author chenlw v1.0 2018/7/10
     */
    public static Document parseText(String text) throws DocumentException, SAXException {
        Document result;

        SAXReader reader = new SAXReader();
        // 禁用DTDs
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        String encoding = getEncoding(text);

        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding(encoding);

        result = reader.read(source);

        // if the XML parser doesn't provide a way to retrieve the encoding,
        // specify it manually
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }
        return result;
    }

    private static String getEncoding(String text) {
        String result = null;

        String xml = text.trim();

        String xmlEnd = "<?xml";

        if (xml.startsWith(xmlEnd)) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();

                if ("encoding".equals(token)) {
                    if (tokens.hasMoreTokens()) {
                        result = tokens.nextToken();
                    }

                    break;
                }
            }
        }

        return result;
    }
}

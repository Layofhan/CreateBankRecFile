package com.han.createbankrecfileapi.model;


import lombok.Data;

@Data
public class XmlObject {
    /**
     * 头节点
     */
    private Head head;
    /**
     * 实时交易报文体节点
     */
    private Req req;
}

package com.han.createbankrecfileapi.common.helper;

public class DataException extends Exception{

    private String detailMessage;

    public DataException(String p_Msg){
        this.detailMessage = p_Msg;
    }

    public String getMessage(){
        return this.detailMessage;
    }
}

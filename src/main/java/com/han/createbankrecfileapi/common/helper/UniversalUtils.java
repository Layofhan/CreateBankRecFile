package com.han.createbankrecfileapi.common.helper;


import cn.hutool.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用工具类 主要包含处理过程的通用类提取
 */
public class UniversalUtils {

    //将对象转化为String类型
    public static String objToStrUtil(Object p_Object){

        if(ObjectUtil.isNull(p_Object)){
            return null;
        }else {
            return p_Object.toString();
        }
    }
    //将对象转化为FLoat
    public static float objToFloatUtil(Object p_Object){

        if(ObjectUtil.isNull(p_Object)){
            return 0;
        }else {
            return Float.parseFloat(p_Object.toString());
        }
    }


    //对应参数为空校验以及长度校验
    public static void checkStringParam(String name,Object value,int maxLen,boolean required) throws Exception {
        if(required){
            // 必填项校验，value不为空，且长度不超过maxLen
            isNotNullAndInLimit(name,value,maxLen);
        }else{
            // 非必填项校验，value可为空，value不为空时不能超过maxLen
            inLimit(name,value,maxLen);
        }
    }

    private static void inLimit(String name, Object value, int maxLen) throws Exception {
        if(ObjectUtil.isNotNull(value)&& value.toString().isEmpty() &&value.toString().length()>maxLen){
            throw new DataException(name+"格式有误，最长"+maxLen+"个字符");
        }
    }

    private static void isNotNullAndInLimit(String name, Object value, int maxLen) throws Exception {
        if(ObjectUtil.isNull(value)|| value.toString().isEmpty()){
            throw new DataException(name+"不能为空");
        }
        if(value.toString().length()>maxLen){
            throw new DataException(name+"格式有误，最长"+maxLen+"个字符");
        }
    }
}

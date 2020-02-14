package com.han.createbankrecfileapi.common.helper;


import com.han.createbankrecfileapi.model.XmlObject;
import com.han.createbankrecfileapi.model.config.BankConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

//校验类 主要存放 3des加密 MD5生成及校验等方法
public class VerifyHelper {

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    //region MD5相关
    /**
     * md5加密
     * @param p_Content
     * @return
     */
    public static String getMD5(String p_Content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(p_Content.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // 如果不满足32位，则将0加入补足32位
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            System.out.println(hashtext);
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion

    //region RSA相关

    /**
     * 校验数字签名
     *
     * @param data 摘要
     * @param sign 数字签名
     * @return boolean
     */
    public static boolean verify(byte[] data, String modulusStr, String exponentStr, String sign) throws Exception {
        PublicKey publicK;
        boolean isVerify;
        publicK = getPublicKey(modulusStr, exponentStr);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        isVerify = signature.verify(decode(sign));
        return isVerify;
    }

    /**
     * 获取RSA公钥
     *
     * @param modulusStr  modulus字符串
     * @param exponentStr exponent字符串
     * @return RSA公钥
     * @throws Exception 异常
     */
    private static PublicKey getPublicKey(String modulusStr, String exponentStr) throws Exception {
        //获取 Modulus
        BigInteger modulus = new BigInteger(1, decode(modulusStr));
        //获取 Exponent
        BigInteger publicExponent = new BigInteger(1, decode(exponentStr));
        //创建公钥对象
        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyf;
        keyf = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyf.generatePublic(rsaPubKey);
    }

    /**
     * 获取私钥
     */
    private static PrivateKey getPrivateKey(String modulusStr,String exponentStr,String pStr,String qStr,String dPStr,String dQStr,String inverseQStr,String dStr) throws Exception {
        byte[] modulusBytes = decode(modulusStr);
        BigInteger modulus = new BigInteger(1, modulusBytes);
        byte[] exponentBytes = decode(exponentStr);
        BigInteger publicExponent = new BigInteger(1, exponentBytes);
        byte[] pBytes = decode(pStr);
        BigInteger primeP = new BigInteger(1, pBytes);
        byte[] qBytes = decode(qStr);
        BigInteger primeQ = new BigInteger(1, qBytes);
        byte[] dpBytes = decode(dPStr);
        BigInteger primeExponentP = new BigInteger(1, dpBytes);
        byte[] dqBytes = decode(dQStr);
        BigInteger primeExponentQ = new BigInteger(1, dqBytes);
        byte[] inverseQBytes = decode(inverseQStr);
        BigInteger crtCoefficient = new BigInteger(1, inverseQBytes);
        byte[] dBytes = decode(dStr);
        BigInteger privateExponent = new BigInteger(1, dBytes);
        RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus,
                publicExponent, privateExponent, primeP, primeQ,
                primeExponentP, primeExponentQ, crtCoefficient);

        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyf.generatePrivate(rsaPriKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成数字签名并进行Base64编码
     */
    public static String signToBase64(byte[] data, String modulusStr,String exponentStr,String pStr,String qStr,String dPStr,String dQStr,String inverseQStr,String dStr) throws Exception {
        return encode(sign(data, modulusStr,exponentStr,pStr,qStr,dPStr,dQStr,inverseQStr,dStr));
    }

    /**
     * Base64字符串解码为字节数组
     * @param base64 Base64字符串
     * @return 解码后的字节数组
     * @throws Exception
     */
    public static byte[] decode(String base64){
        return Base64.decodeBase64(base64);
    }

    /**
     * 字节数组编码成Base64字符串
     * @param bytes 字节数组
     * @return Base64字符串
     * @throws Exception
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
    /**
     * 生成数字签名
     */
    public static byte[] sign(byte[] data, String modulusStr,String exponentStr,String pStr,String qStr,String dPStr,String dQStr,String inverseQStr,String dStr) throws Exception {
        PrivateKey privateK = getPrivateKey(modulusStr,exponentStr,pStr,qStr,dPStr,dQStr,inverseQStr,dStr);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return signature.sign();
    }
    //endregion

    /**
     * 数据验签
     * @param p_Md5Content 需要验签的MD5，自己生成的
     * @param p_VerifyCode RSA加密后的MD5
     * @param p_BankConfig 银行配置
     */
    public static boolean verifyData(String p_Md5Content, String p_VerifyCode, BankConfig p_BankConfig)
            throws Exception{
        try {
            //region 测试使用
//            String modulus = "rLPl8MDJvMkDkNHh0yd6NzbxrdVZ7Pry/GlYQO3QD3j7p/XGNoYgKNkL1s7/xxqQUvbxItGeoijYfTrdQQQySFchEUR6JgWp5dZy2a2YVnXteGvk+qelApUrF8I+gypsaUPl/7uAlW5CT79zkQ5wYkjurvNLlynYDpgsmKsrgJE=";
//            String exponent = "AQAB";
//            String p = "02qPkcYDBDp8r2U1EqoL/RJrM3UhSNZbssvHomYWbLrzXEiRiHy9pNEOcPlq2PBJxD137R6qr9FOkJqvequLBw==";
//            String q = "0R9dBtYc77DA6gtKDZuktgyNP0MXS4LzvFEqtq9BjBZ/JBXHolJCudYGkAp4AhxCETD0thf11J9gynzUmCb5pw==";
//            String dp = "nzYejH78ApExGL007KtWf+0BAi1xNXMId2tzGd+bf6KCZrrXrluSTa6KG5YZWuoKA1jvGYkArYsIiWmUQOMyvQ==";
//            String dq = "rWGTf1eGJjQlveYeP6oLpeRCN3EonzKzYi7pew1TCxKb1w83tRz+tZT9W+9SEG3dWON+AHtdFiwN09QrbvrhgQ==";
//            String inverseq = "KRDkoR39i+cn6r/W0dFjICuNHKG9R0Wky6lXWtHPUqSCBuPZDzueN6wD7OboH2H5JxOTXGIBmP6NAef8+FEpxQ==";
//            String d = "GTCYLliAVFfhfEMQtDLC4cDS2W8QEb+8p1JaPAYz3b3gvuvbQGKp2CtoTamdpxXZzTAVleWLRM2+Xe2zDeOlzagI4aFr3ejCrVlQuZJ5ZpIWepYGkDwuQ3Aeihhfl9xBZRK5+3KJZFuFYbkbIwZ93m60W3fAxwTzIfIP3BQojn0=";
            //endregion
            //公钥解密，并进行MD5比对
            boolean isVerify = verify(p_Md5Content.getBytes(Charset.forName("GBK"))
                    , p_BankConfig.getPublicKeyModulus()
                    , p_BankConfig.getPublicKeyExponent()
                    , p_VerifyCode);

            return isVerify;
        }catch (Exception ex){
           throw new Exception("验签代码出现错误，错误信息：" + ex.getMessage());
        }

    }

    /**
     * 加密
     * @param p_Content 需要加密的原始内容
     * @param p_BankConfig 银行配置
     * @throws Exception
     */
    public static String sign(String p_Content, BankConfig p_BankConfig)
            throws Exception{
        try {
            //加密
            String signData = signToBase64(p_Content.getBytes(Charset.forName("GBK"))
                    , p_BankConfig.getPrivateKeyModulus()
                    , p_BankConfig.getPrivateKeyExponent()
                    , p_BankConfig.getP()
                    , p_BankConfig.getQ()
                    , p_BankConfig.getDP()
                    , p_BankConfig.getDQ()
                    , p_BankConfig.getInverseQ()
                    , p_BankConfig.getD());

            return signData;
        }catch (Exception ex){
            throw new Exception("私钥签名出现错误，错误信息：" + ex.getMessage());
        }
    }
}

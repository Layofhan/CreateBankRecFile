package com.han.createbankrecfileapi.controller;

import com.han.createbankrecfileapi.Globals;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyThread implements Runnable{

    //private ForWard forWard = Globals.getBean("ForWard", ForWard.class);
    private ChannelHandlerContext channelHandlerContext;
    private  Object msgTemp;

    public NettyThread(ChannelHandlerContext _temp,Object _msgTemp){
        this.msgTemp = _msgTemp;
        this.channelHandlerContext = _temp;
    }

    public void run() {
        ByteBuf buf = (ByteBuf) msgTemp;
        byte[] buffer = new byte[buf.readableBytes()];
        buf.readBytes(buffer, 0, buffer.length);
        String rev = new String(buffer);
        log.info("服务端收到客户端的数据:" + rev);
        try {
            //Thread.sleep(1500);
            String returnMsg = GetData(rev);
            log.info("返回信息"+returnMsg);
            //String returnMsg = rev;
            this.channelHandlerContext.writeAndFlush(returnMsg);
        }
        catch (InterruptedException ex){
            log.error("【错误】"+ex.getMessage());
            this.channelHandlerContext.writeAndFlush("【错误】"+ex.getMessage());
        }
        catch (Exception e){
            log.error("【错误】"+e.getMessage());
            this.channelHandlerContext.writeAndFlush("【错误】"+e.getMessage());
        }
    }

    public String GetData(String msg) throws Exception{
        try{
            String commandCode = msg.substring(0, 5);
            String xmlReqWithoutCommandCode = msg.substring(5, msg.length());
            ForWard forWard = (ForWard)Globals.getBean(ForWard.class);
            String requestIp = this.channelHandlerContext.channel().localAddress().toString();
            requestIp = requestIp.substring(1,requestIp.lastIndexOf(":"));
            String returnXml = forWard.Deal(commandCode,xmlReqWithoutCommandCode,requestIp);
            return returnXml;
        }
        catch (Exception ex){
            throw ex;
        }
    }

}

package com.han.createbankrecfileapi.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //private int ThreadsNums = 100;//设置最大得线程处理数量
    /**
     * channelAction channel 通道 action 活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println(ctx.channel().localAddress().toString() + " 通道已激活！");
        log.info(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /**
     * channelInactive channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println(ctx.channel().localAddress().toString() + " 通道不活跃！并且关闭。");
        log.info(ctx.channel().localAddress().toString() + " 通道不活跃！并且关闭。");
        // 关闭流
        ctx.close();
    }

    /**
     * 功能：读取服务器发送过来的信息
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 第一种：接收字符串时的处理
        //创建一个具有固定线程数的线程池
        BootInit.pool.submit(new NettyThread(ctx,msg));
        int threadCount = ((ThreadPoolExecutor)BootInit.pool).getActiveCount();
        //System.out.println("1.threadCount===="+threadCount +ctx.channel().localAddress().toString());
    }

    /**
     * 功能：读取完毕客户端发送过来的数据之后的操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("服务端接收数据完毕..");
        // 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
//         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//         ctx.flush();
//         ctx.flush(); //
        // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
        // ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
    }

    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        //System.out.println("异常信息：\r\n");
        //cause.printStackTrace();
        log.error("异常信息："+cause.getMessage() + ";位置：" + cause.getStackTrace());
    }
}

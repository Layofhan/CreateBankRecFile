package com.han.createbankrecfileapi.controller;

import cn.hutool.core.util.ObjectUtil;
import com.han.createbankrecfileapi.common.helper.ConfigHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;


import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class NettyServer {
    private static NettyServer nettyServer = null;
    private String Ip;
    private int Port;
    public NettyServer(){
        BootInit.pool.execute(()->{
            try{
                //获取配置信息
                if(ObjectUtil.isEmpty(ConfigHelper.GetInstance().Ip)){
                    this.Ip = "127.0.0.1";
                }else {
                    this.Ip = ConfigHelper.GetInstance().Ip;
                }
                if(ObjectUtil.isEmpty(ConfigHelper.GetInstance().Port)){
                    this.Port = 7063;
                }else {
                    this.Port =  Integer.parseInt(ConfigHelper.GetInstance().Port);
                }
                log.info("Netty服务器port为:{}，开始启动服务器",Port);
                start();
            }
            catch (Exception ex){
                log.error("netty启动出现错误，错误信息:"+ex.getMessage());
            }
        });
    }

    public static void initServerInstance(){
        if (nettyServer == null) {
            synchronized (NettyServer.class) {
                if (nettyServer == null) {
                    log.info("第一次初始化Server实例");
                    nettyServer = new NettyServer();
                }
            }
        }
    }

    public void start() throws Exception {
        // 创建用于监听accept的线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建用于处理队列和数据的线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try { // netty服务器的创建, 辅助工具类，用于服务器通道的一系列配置
            ServerBootstrap bootstrap = new ServerBootstrap();

            // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时
            // ，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

            // SO_REUSEADDR允许启动一个监听服务器并捆绑其众所周知端口，
            // 即使以前建立的将此端口用做他们的本地端口的连接仍存在。
            // 这通常是重启监听服务器时出现，若不设置此选项，则bind时将出错。
            // SO_REUSEADDR允许在同一端口上启动同一服务器的多个实例，
            // 只要每个实例捆绑一个不同的本地IP地址即可。对于TCP，
            // 我们根本不可能启动捆绑相同IP地址和相同端口号的多个服务器。
            // SO_REUSEADDR允许单个进程捆绑同一端口到多个套接口上，
            // 只要每个捆绑指定不同的本地IP地址即可。这一般不用于TCP服务器。
            // SO_REUSEADDR允许完全重复的捆绑：当一个IP地址和端口绑定到某个套接口上时，
            // 还允许此IP地址和端口捆绑到另一个套接口上。一般来说，这个特性仅在支持多播的系统上才有，
            // 而且只对UDP套接口而言（TCP不支持多播）
            bootstrap.option(ChannelOption.SO_REUSEADDR, true);

            // Netty4使用对象池，重用缓冲区
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1341, 65536));
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);

            // 绑定线程池
            bootstrap.group(group, bossGroup);
            // 指定使用的channel
            bootstrap.channel(NioServerSocketChannel.class);
            // 绑定监听端口
            ;
            // 绑定客户端连接时候触发操作
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    log.info("信息：有一客户端链接到本服务端;IP:" + ch.localAddress().getAddress() + "Port:" + ch.localAddress().getPort());
                    ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));//设置读取格式
                    //System.out.println("客户端触发操作");
                    ch.pipeline().addLast(new NettyServerHandler()); // 客户端触发操作
                    ch.pipeline().addLast(new ByteArrayEncoder());
                }
            });
            ChannelFuture cf = bootstrap.bind(this.Ip,this.Port).sync(); // 服务器异步创建绑定
            //System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            // 释放线程池资源
            group.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }
}

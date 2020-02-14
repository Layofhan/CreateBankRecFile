package com.han.createbankrecfileapi.controller;

import com.han.createbankrecfileapi.common.helper.ConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class BootInit {

    public static ExecutorService pool = Executors.newFixedThreadPool(100);//ThreadsNums

    //程序启动入口类，之后需要在程序启动时初始化的内容 可以放在这边处理
    @PostConstruct
    public void run(){
        //加载配置
        ConfigHelper.GetInstance();
        ///这边有个问题没有解决，需求：需要先加载完配置在启动netty
        //启动netty
        NettyServer.initServerInstance();

    }
}

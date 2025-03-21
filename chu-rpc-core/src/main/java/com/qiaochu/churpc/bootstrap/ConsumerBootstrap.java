package com.qiaochu.churpc.bootstrap;

import com.qiaochu.churpc.RpcApplication;

/**
 * 消费端启动类
 */
public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }

}

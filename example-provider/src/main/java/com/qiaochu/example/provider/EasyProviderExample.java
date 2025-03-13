package com.qiaochu.example.provider;

import com.qiaochu.churpc.RpcApplication;
import com.qiaochu.churpc.registry.LocalRegistry;
import com.qiaochu.churpc.server.HttpServer;
import com.qiaochu.churpc.server.VertxHttpServer;
import com.qiaochu.example.common.service.UserService;

/**
 * 简单服务提供者实例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getConfig().getServerPort());
    }
}

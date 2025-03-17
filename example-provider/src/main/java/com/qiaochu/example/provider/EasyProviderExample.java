package com.qiaochu.example.provider;

import com.qiaochu.churpc.RpcApplication;
import com.qiaochu.churpc.config.RegistryConfig;
import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.model.ServiceMetaInfo;
import com.qiaochu.churpc.registry.LocalRegistry;
import com.qiaochu.churpc.registry.Registry;
import com.qiaochu.churpc.registry.RegistryFactory;
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
        String serviceName = UserService.class.getName();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getConfig().getServerPort());
    }
}

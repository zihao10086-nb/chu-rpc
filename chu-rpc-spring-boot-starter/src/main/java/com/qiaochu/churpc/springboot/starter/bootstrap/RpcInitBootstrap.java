package com.qiaochu.churpc.springboot.starter.bootstrap;

import com.qiaochu.churpc.RpcApplication;
import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.server.tcp.VertxTcpClient;
import com.qiaochu.churpc.server.tcp.VertxTcpServer;
import com.qiaochu.churpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        //获取EnableRpc注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");
        //RPC框架初始化 (配置和注册中心)
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getConfig();
        if (needServer){
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else {
            log.info("needServer is false, not start server");
        }
    }
}

package com.qiaochu.example.consumer;

import com.qiaochu.churpc.RpcApplication;
import com.qiaochu.churpc.config.RegistryConfig;
import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.model.ServiceMetaInfo;
import com.qiaochu.churpc.registry.Registry;
import com.qiaochu.churpc.registry.RegistryFactory;
import com.qiaochu.churpc.utils.ConfigUtils;

import java.util.List;

/**
 * 简易服务消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) throws InterruptedException {

        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        RegistryConfig registryConfig = new RegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        List<ServiceMetaInfo> serviceMetaInfos1 = registry.serviceDiscovery("");
        System.out.println(serviceMetaInfos1);
        serviceMetaInfos1 = registry.serviceDiscovery("com.qiaochu");
        System.out.println(serviceMetaInfos1);
        Thread.sleep(10000);
        serviceMetaInfos1= registry.serviceDiscovery("");
        System.out.println(serviceMetaInfos1);

        System.out.println(rpcConfig);
    }
}

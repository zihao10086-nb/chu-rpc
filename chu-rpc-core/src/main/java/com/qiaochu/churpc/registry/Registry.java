package com.qiaochu.churpc.registry;

import com.qiaochu.churpc.config.RegistryConfig;
import com.qiaochu.churpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口
 */
public interface Registry {
    /**
     * 初始化注册中心
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * 服务端
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * 服务端
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) ;

    /**
     * 服务发现 获取某服务的所有节点
     * 消费端
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) ;

    /**
     * 销毁服务
     */
    void destroy();

    /**
     * 心跳检测
     * 服务端
     */
    void heartBeat();

    /**
     * 监听服务变化
     * 消费端
     */
    void watch(String serviceNodeKey);
}

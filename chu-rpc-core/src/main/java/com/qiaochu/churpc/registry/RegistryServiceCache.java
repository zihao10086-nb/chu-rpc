package com.qiaochu.churpc.registry;

import com.qiaochu.churpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心 本地缓存
 */
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param serviceCache
     */
    void writeCache(List<ServiceMetaInfo> serviceCache){
        this.serviceCache = serviceCache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache(){
        return serviceCache;
    }

    /**
     * 清除缓存
     */
    void clearCache(){
        this.serviceCache = null;
    }
}

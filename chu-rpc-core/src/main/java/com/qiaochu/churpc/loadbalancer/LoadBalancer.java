package com.qiaochu.churpc.loadbalancer;

import com.qiaochu.churpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器
 * 消费端使用
 */
public interface LoadBalancer {
    /**
     * 选择服务节点
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}

package com.qiaochu.churpc.loadbalancer;

import com.qiaochu.churpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性Hash负载均衡算法
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {
    /**
     * 一致性哈希环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();
    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }

        //构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() +"#"+ i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        //获取调用请求的哈希值
        int hash =getHash(requestParams);
        //获取虚拟节点环中顺时针最近的虚拟节点
        Map.Entry<Integer,ServiceMetaInfo> entry=virtualNodes.ceilingEntry(hash);
        if (entry== null){
            //如果没有大于等于调用请求hash值的虚拟节点，则返回环首部的节点
            entry=virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 获取哈希值
     * 可以根据需求实现自己的哈希算法
     * @param key
     * @return
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}

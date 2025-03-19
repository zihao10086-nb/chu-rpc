package com.qiaochu.churpc.loadbalancer;

import com.qiaochu.churpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡算法
 */
public class RandomLoadBalancer implements LoadBalancer{

    private final Random random=new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0){
            return null;
        }
        //只有一个服务，无需随机
        if (size == 1){
            return serviceMetaInfoList.get(0);
        }
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}

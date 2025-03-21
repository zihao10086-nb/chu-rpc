package com.qiaochu.churpc.fault.tolerant;

import com.qiaochu.churpc.loadbalancer.LoadBalancer;
import com.qiaochu.churpc.loadbalancer.RoundRobinLoadBalancer;
import com.qiaochu.churpc.spi.SpiLoader;

/**
 * 容错策略工厂
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 获取默认的容错策略
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取容错策略实例
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}

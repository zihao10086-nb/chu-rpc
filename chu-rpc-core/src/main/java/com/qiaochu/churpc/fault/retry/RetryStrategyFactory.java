package com.qiaochu.churpc.fault.retry;


import com.qiaochu.churpc.spi.SpiLoader;

/**
 * 重试策略工厂
 */
public class RetryStrategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认的重试策略
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();
    /**
     * 获取实例
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}

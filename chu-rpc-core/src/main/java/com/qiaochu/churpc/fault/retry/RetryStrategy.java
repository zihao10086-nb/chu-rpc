package com.qiaochu.churpc.fault.retry;

import com.qiaochu.churpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略接口
 */
public interface RetryStrategy {
    /**
     * 重试策略
     * @param callable
     * @return
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}

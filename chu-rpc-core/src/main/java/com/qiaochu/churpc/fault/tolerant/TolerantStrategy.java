package com.qiaochu.churpc.fault.tolerant;

import com.qiaochu.churpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 */
public interface TolerantStrategy {
    /**
     * 容错
     * @param context 上下文
     * @param e 异常
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);

}

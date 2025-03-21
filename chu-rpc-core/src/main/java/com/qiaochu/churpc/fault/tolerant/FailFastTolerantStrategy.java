package com.qiaochu.churpc.fault.tolerant;

import com.qiaochu.churpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}

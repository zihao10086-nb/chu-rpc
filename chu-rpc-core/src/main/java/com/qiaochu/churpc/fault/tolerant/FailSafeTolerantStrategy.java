package com.qiaochu.churpc.fault.tolerant;

import com.qiaochu.churpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理",e);
        return new RpcResponse();
    }
}

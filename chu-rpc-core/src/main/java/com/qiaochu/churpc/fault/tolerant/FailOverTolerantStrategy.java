package com.qiaochu.churpc.fault.tolerant;

import com.qiaochu.churpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 故障转移
 * 待实现
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy{
    
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 获取其他服务节点并调用
        return null;
    }
}

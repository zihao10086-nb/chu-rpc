package com.qiaochu.churpc.bootstrap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegisterInfo<T>{
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务实现类
     */
    private Class<? extends T> implClass;
}

package com.qiaochu.churpc;

import com.qiaochu.churpc.config.RegistryConfig;
import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.constant.RpcConstant;
import com.qiaochu.churpc.registry.Registry;
import com.qiaochu.churpc.registry.RegistryFactory;
import com.qiaochu.churpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * Rpc 框架应用
 * 相当于holder 存放了项目全局用到的变量。双检锁单例模式实现
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig config;

    /**
     * 框架初始化 ，支持传入自定义配置
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        config = newRpcConfig;
        log.info("rpc init ,config={}", newRpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig = config.getRegistryConfig();
        Registry registry= RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init ,config={}", registryConfig);
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            //配置加载失败 使用默认值
            newRpcConfig=new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     * @return
     */
    public static RpcConfig getConfig() {
        if (config == null) {
            synchronized (RpcApplication.class) {
                if (config == null) {
                    init();
                }
            }
        }
        return config;
    }

}

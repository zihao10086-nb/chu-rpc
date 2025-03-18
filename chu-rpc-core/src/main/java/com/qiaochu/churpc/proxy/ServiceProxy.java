package com.qiaochu.churpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qiaochu.churpc.RpcApplication;
import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.constant.RpcConstant;
import com.qiaochu.churpc.model.RpcRequest;
import com.qiaochu.churpc.model.RpcResponse;
import com.qiaochu.churpc.model.ServiceMetaInfo;
import com.qiaochu.churpc.protocol.*;
import com.qiaochu.churpc.registry.Registry;
import com.qiaochu.churpc.registry.RegistryFactory;
import com.qiaochu.churpc.serializer.JdkSerializer;
import com.qiaochu.churpc.serializer.Serializer;
import com.qiaochu.churpc.serializer.SerializerFactory;
import com.qiaochu.churpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理 （JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //序列化
            byte[] bytes = serializer.serialize(rpcRequest);

            //注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)){
                throw new Exception("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);
            //发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, serviceMetaInfo);
            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.qiaochu.churpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qiaochu.churpc.model.RpcRequest;
import com.qiaochu.churpc.model.RpcResponse;
import com.qiaochu.churpc.serializer.JdkSerializer;
import com.qiaochu.churpc.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        Serializer serializer = new JdkSerializer();
        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //序列化
            byte[] bytes = serializer.serialize(rpcRequest);
            //发送请求
            //todo 这里地址被硬编码了，需要使用注册中心和服务发现机制解决
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                    .body(bytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

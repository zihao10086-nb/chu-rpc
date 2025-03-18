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

            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            System.out.println("Connected to TCP server");
                            NetSocket socket = result.result();
                            //发送数据
                            //构造请求
                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.MESSAGE_MAGIC);
                            header.setVersion(ProtocolConstant.MESSAGE_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);
                            //编码请求
                            try {
                                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encode);
                            } catch (IOException e) {
                                throw new RuntimeException("协议信息编码错误");
                            }

                            //接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(decode.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议信息解码错误");
                                }
                            });

                        }else {
                            System.out.println("Failed to connect to TCP server");
                        }
                    });
            RpcResponse rpcResponse = responseFuture.get();
            //关闭连接
            netClient.close();
            return rpcResponse.getData();
//            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

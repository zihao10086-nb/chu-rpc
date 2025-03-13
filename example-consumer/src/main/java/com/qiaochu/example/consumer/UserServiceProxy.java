package com.qiaochu.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qiaochu.churpc.model.RpcRequest;
import com.qiaochu.churpc.model.RpcResponse;
import com.qiaochu.churpc.serializer.JdkSerializer;
import com.qiaochu.churpc.serializer.Serializer;
import com.qiaochu.example.common.model.User;
import com.qiaochu.example.common.service.UserService;

/**
 * 静态代理
 */
@Deprecated
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer = new JdkSerializer();
        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result ;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                    .body(bytes)
                    .execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

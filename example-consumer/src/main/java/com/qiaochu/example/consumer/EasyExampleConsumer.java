package com.qiaochu.example.consumer;

import com.qiaochu.churpc.config.RpcConfig;
import com.qiaochu.churpc.proxy.ServiceProxyFactory;
import com.qiaochu.churpc.registry.Registry;
import com.qiaochu.churpc.serializer.Serializer;
import com.qiaochu.churpc.utils.ConfigUtils;
import com.qiaochu.example.common.model.User;
import com.qiaochu.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单服务消费者示例
 */
@Slf4j
public class EasyExampleConsumer {

    public static void main(String[] args) {

//        //静态代理
//        UserService userService = new UserServiceProxy();
        //动态代理
        User user = new User();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        user.setName("qiaochu");
        //调用
        User newUser = userService.getUser(user);
        if (newUser!= null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }

        short number = userService.getNumber();
        System.out.println(number);


    }
}

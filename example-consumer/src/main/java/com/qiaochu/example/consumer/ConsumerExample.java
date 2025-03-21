package com.qiaochu.example.consumer;

import com.qiaochu.churpc.bootstrap.ConsumerBootstrap;
import com.qiaochu.churpc.proxy.ServiceProxyFactory;
import com.qiaochu.example.common.model.User;
import com.qiaochu.example.common.service.UserService;

/**
 * 简易服务消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}

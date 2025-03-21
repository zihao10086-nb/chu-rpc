package com.qiaochu.example.provider;

import com.qiaochu.churpc.bootstrap.ProviderBootstrap;
import com.qiaochu.churpc.bootstrap.ServiceRegisterInfo;
import com.qiaochu.example.common.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample {
    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}

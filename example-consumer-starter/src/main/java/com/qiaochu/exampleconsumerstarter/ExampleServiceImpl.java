package com.qiaochu.exampleconsumerstarter;

import com.qiaochu.example.common.model.User;
import com.qiaochu.example.common.service.UserService;
import com.qiaochu.churpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("yupi");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}

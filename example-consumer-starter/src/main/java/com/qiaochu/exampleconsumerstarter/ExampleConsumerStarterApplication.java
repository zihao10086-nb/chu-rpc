package com.qiaochu.exampleconsumerstarter;

import com.qiaochu.churpc.springboot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = false)
public class ExampleConsumerStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleConsumerStarterApplication.class, args);
    }

}

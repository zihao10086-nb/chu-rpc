package com.qiaochu.exampleproviderstarter;

import com.qiaochu.churpc.springboot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class ExampleProviderStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleProviderStarterApplication.class, args);
    }

}

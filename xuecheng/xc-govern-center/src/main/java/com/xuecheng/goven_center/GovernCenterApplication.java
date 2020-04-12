package com.xuecheng.goven_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // 启用EurekaServer组件
public class GovernCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class, args);
    }
    
}

package com.xuecheng.manage_cms_client;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.xuecheng.framework")
@ComponentScan(basePackages = "com.xuecheng.manage_cms_client")
@EnableRabbit
public class CmsManageClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsManageClientApplication.class,args);
    }

}

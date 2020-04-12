package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(basePackages = "com.xuecheng.framework.domain.cms") // 扫描实体类
@ComponentScan(basePackages = "com.xuecheng.api")   // 扫描api接口
@EnableMongoRepositories(basePackages = "com.xuecheng.manage_cms.dao") //扫描Mongodb的dao接口
@ComponentScan(basePackages = "com.xuecheng.manage_cms")    // 扫描本项目下的类
@ComponentScan(basePackages = "com.xuecheng.framework") // 扫描common包下的类
@ComponentScan(basePackages = "com.xuecheng.framework.domain.system") // 扫描common包下的类
@EnableDiscoveryClient // 启用Eureka服务发现功能
public class CmsManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsManageApplication.class);
    }

    @Bean
//    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}

package com.xuecheng.manage_course;

import com.xuecheng.framework.interceptor.FeignClientInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EntityScan("com.xuecheng.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.manage_course"})
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
@ComponentScan(basePackages={"com.xuecheng.framework.domain.course"})//扫描实体类
@EnableDiscoveryClient
@EnableFeignClients  // 启用Feign组件 , 扫描接口上有FeignClient注解的类,为其实现动态代理
public class ManageCourseApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }

    @Bean
    public FeignClientInterceptor createFeignClientInterceptor(){
        return new FeignClientInterceptor();
    }
}

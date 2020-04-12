package com.xuecheng.manage_cms_client.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xuecheng.mq")
@Data
@NoArgsConstructor
public class RabbitmqConfig {
    // 队列bean名称
    public static final String QUEUE_CMS_PORTAL = "queue_cms_portal";
    public static final String QUEUE_CMS_COURSE = "queue_cms_course";
    //交换机名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";
    // 队列名称
    public String portalQueue ;
    public String courseQueue ;
    //routingKey
    public String portalRoutingKey;
    public String courseRoutingKey;

    // 创建Queue对象并添加至spring容器中
    @Bean(QUEUE_CMS_PORTAL) // 门户队列
    public Queue QUEUE_CMS_PORTAL(){

        return new Queue(portalQueue,true);
    }

    @Bean(QUEUE_CMS_COURSE) // 课程队列
    public Queue QUEUE_CMS_COURSE(){
        return new Queue(courseQueue,true);
    }

    // 创建Exchange对象并添加到Spring容器中
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    // 将门户队列与交换机进行绑定
    @Bean
    public Binding bindingCourse(@Qualifier(QUEUE_CMS_PORTAL)Queue queue,
                           @Qualifier(EX_ROUTING_CMS_POSTPAGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(portalRoutingKey).noargs();
    }

    @Bean // 将课程队列绑定交换机
    public Binding bindingPortal(@Qualifier(QUEUE_CMS_COURSE)Queue queue,
                                 @Qualifier(EX_ROUTING_CMS_POSTPAGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(courseRoutingKey).noargs();
    }

}

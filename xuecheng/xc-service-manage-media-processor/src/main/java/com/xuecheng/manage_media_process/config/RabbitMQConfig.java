package com.xuecheng.manage_media_process.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:04
 **/
@Configuration
public class RabbitMQConfig {

    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    //视频处理队列
    @Value("${xc-service-manage-media.mq.queue-media-video-processor}")
    public  String queue_media_video_processtask;

    //视频处理路由Key
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;


    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    }
    //声明队列
    @Bean("queue_media_video_processtask")
    public Queue QUEUE_PROCESSTASK() {
        Queue queue = new Queue(queue_media_video_processtask,true,false,true);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier("queue_media_video_processtask") Queue queue, @Qualifier(EX_MEDIA_PROCESSTASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }


    // 配置容器工厂, 开启多个进程监听mq
    @Bean("simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory
                customerContainer(SimpleRabbitListenerContainerFactoryConfigurer configurer,    // 容器工厂配置类
                                  ConnectionFactory connectionFactory       //  RabbitLisenter连接工厂
                                ){
        // 创建容器工厂
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory
                = new SimpleRabbitListenerContainerFactory();
        // 设置起始监听进程数量
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        // 设置最大监听进程数量
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);

        // 使用SimpleRabbitListenerContainerFactoryConfigurer容器工厂配置类 将容器工厂与连接工厂建立关联
        configurer.configure(simpleRabbitListenerContainerFactory,connectionFactory);

        return simpleRabbitListenerContainerFactory;
    }




}

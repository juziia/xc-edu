package com.xuecheng.manage_cms_client.listener;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.courseQueue}","${xuecheng.mq.portalQueue}"})
    public void postPortalPage(String msgJson){
        // 消息内容为json格式,序列化为Map
        Map<String,String> map = JSON.parseObject(msgJson, Map.class);
        String pageId = map.get("pageId");
        if(StringUtils.isNotBlank(pageId)){
            pageService.savePageToServerPath(pageId);
        }else{
            LOGGER.error("消息接收错误! pageId is blank : {}",pageId);
        }

    }


}

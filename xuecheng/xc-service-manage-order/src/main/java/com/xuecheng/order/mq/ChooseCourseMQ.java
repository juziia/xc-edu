package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChooseCourseMQ {

    @Autowired
    private TaskService taskService;

    /**
     * 任务完成,将当前任务删除, 并且添加到历史任务中
     */
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void taskSuccess(XcTaskHis xcTaskHis){
        if(xcTaskHis == null || StringUtils.isBlank(xcTaskHis.getId())){
            return ;
        }

        taskService.taskSuccess(xcTaskHis);

    }


}

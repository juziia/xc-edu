package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.CourseLearningService;
import com.xuecheng.learning.service.LearningCourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LearningCourseTask {

    @Autowired
    private LearningCourseService learningCourseService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 接收订单消息,添加课程任务
     */
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void addCourseTask(XcTask xcTask) {
        if (xcTask == null) {
            return;
        }
        String requestBody = xcTask.getRequestBody();
        if (StringUtils.isBlank(requestBody)) {
            return;
        }

        Map map = null;
        try {
            map = JSON.parseObject(requestBody, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");

        XcTaskHis xcTaskHis = this.learningCourseService.addLearningCourse(userId, courseId, null, null, null, xcTask);
        if(xcTaskHis != null){
            // 添加选课完成,向订单服务发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,xcTaskHis);
        }
    }
}

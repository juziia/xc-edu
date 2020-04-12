package com.xuecheng.order.service.impl;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.TaskHisRepository;
import com.xuecheng.order.dao.TaskRepository;
import com.xuecheng.order.service.TaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TaskHisRepository taskHisRepository;

    @Override
    public List<XcTask> findByXcTask( Date date,int size) {
        Pageable pageable = PageRequest.of(0, size);
        Page<XcTask> taskPage = taskRepository.findByUpdateTimeBefore(pageable, date);
        return taskPage.getContent();
    }

    @Override
    @Transactional
    public int publish(XcTask xcTask, String ex, String routingKey) {
        if(xcTask.getId() == null || xcTask.getId() == ""){
            return 0;
        }
        // 向mq发送消息
        rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
        // 修改消息发送时间
        return taskRepository.updateTaskUpdateTime(xcTask.getId(),new Date());
    }

    @Override
    @Transactional
    public int updateTaskVersion(String taskId, int version) {
        return this.taskRepository.updateTaskVersion(taskId,version);
    }

    @Override
    @Transactional
    public void taskSuccess(XcTaskHis xcTaskHis) {
        String id = xcTaskHis.getId();
        Optional<XcTask> optional = taskRepository.findById(id);
        if(optional.isPresent()){
            // 删除
            taskRepository.deleteById(id);
            // 添加到历史任务
            taskHisRepository.save(xcTaskHis);
        }


    }
}

package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TaskService {

    List<XcTask> findByXcTask( Date date,int size);

    int publish(XcTask xcTask,String ex, String routingKey);

    int updateTaskVersion(String taskId,int version);

    void taskSuccess(XcTaskHis xcTaskHis);
}

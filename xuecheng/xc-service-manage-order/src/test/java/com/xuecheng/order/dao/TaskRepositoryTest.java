package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void test(){
        //List<XcTask> list = taskRepository.findByUpdateTimeBefore(0,3,);
       // System.out.println(list);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test001(){
        taskRepository.updateTaskUpdateTime("10",new Date());
    }

}

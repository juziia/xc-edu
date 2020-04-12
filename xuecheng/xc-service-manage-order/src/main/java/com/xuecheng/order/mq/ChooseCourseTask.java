package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0/5 * * * * *")
    public void taskSendMsg() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        // 设置为1分钟之前
        calendar.set(GregorianCalendar.MINUTE, 1);
        // 获取1分钟前的10条任务
        List<XcTask> taskList = taskService.findByXcTask(calendar.getTime(),10);
        if (taskList == null || taskList.isEmpty()) {
            return;
        }
        for (XcTask xcTask : taskList) {
            // 使用乐观锁来获取任务  判断版本号是否修改成功,修改成功则获取任务成功,未成功则获取任务失败
            if(taskService.updateTaskVersion(xcTask.getId(),xcTask.getVersion()) > 0) {
                // 根据消息的交换机与routingKey发送任务消息内容
                taskService.publish(xcTask, xcTask.getMqExchange(), xcTask.getMqRoutingkey());
            }
        }
    }




    //@Scheduled(cron = "0/3 * * * * *")  // 当前方法每隔3秒执行一次
    public void task01(){
        System.out.println("==================任务1开始执行=========================");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==================任务1执行完毕=========================");
    }

    /**
     *  标识该方法是一个定时执行的方法
     *      corn: 定时执行策略
     *      fixedDelay:
     *      */

    //@Scheduled(cron = "0/3 * * * * * ")
    public void task02(){
        System.out.println("==================任务2开始执行=========================");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==================任务2执行完毕=========================");

    }

}

package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;

import java.util.Date;

public interface LearningCourseService {

    XcTaskHis addLearningCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask);
}
package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CourseView;

public interface CourseViewService {
    CourseView courseView(String courseId);

    String preview(String courseId);

    String publish(String courseId);
}

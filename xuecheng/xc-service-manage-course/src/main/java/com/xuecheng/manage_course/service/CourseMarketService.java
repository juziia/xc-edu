package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;

public interface CourseMarketService {

    public CourseMarket addCourseMarket(String courseId, CourseMarket courseMarket);

    CourseMarket findById(String courseId);
}

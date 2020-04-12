package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseBaseService {

    public List<CourseInfo> findByPage(int page, int size, CourseListRequest courseListRequest);

    public Long findCount(CourseListRequest courseListRequest);

    void addCourseBase(CourseBase courseBase);

    CourseBase findById(String courseId);

    CourseBase updateCourseBase(String courseId, CourseBase courseBase);


    void updateCourseBaseStatus(String id,String status);
}

package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;

public interface CoursePicService {

    CoursePic findPicList(String courseId);

    void addPic(String courseId, String pic);

    void deletePic(String courseId);
}

package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoursePicMapper {

    CoursePic findByCourseId(String courseId);

    int addPic(@Param("courseId") String courseId,@Param("pic") String pic);

    int deletePic(String courseId);
}

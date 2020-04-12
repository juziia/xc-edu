package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMarketMapper {

    void save(CourseMarket courseMarket);

    CourseMarket findById(String courseId);

    void update(@Param("courseId") String courseId,@Param("courseMarket") CourseMarket courseMarket);
}

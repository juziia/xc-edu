package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachplanMapper {

    TeachplanNode findList(String courseId);

    List<Teachplan> findByCourseIdAndParentId(@Param("courseId") String courseId, @Param("parentId") String parentId);

    void addTeachPlan(Teachplan rootTeachplan);

    Teachplan finById(String id);
}

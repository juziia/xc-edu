package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.service.CourseBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeachplanMapperTest {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseService courseBaseService;

    @Test
    public void testFindList(){
        TeachplanNode teachplanMapperList = teachplanMapper.findList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanMapperList);
    }

    @Test
    public void testAdd(){
        CourseBase courseBase = courseBaseService.findById("402809817121b742017121c4059d0004");

        // 获取课程信息名称
        String courseBaseName = courseBase.getName();
        // 创建一个根课程计划对象,存数数据库中
        Teachplan rootTeachplan = new Teachplan();

        rootTeachplan.setPname(courseBaseName);
        rootTeachplan.setParentid("0");
        rootTeachplan.setGrade("1");
        rootTeachplan.setCourseid(courseBase.getId());
        rootTeachplan.setOrderby(1);
        rootTeachplan.setStatus("0");
        teachplanRepository.save(rootTeachplan);
    }
}

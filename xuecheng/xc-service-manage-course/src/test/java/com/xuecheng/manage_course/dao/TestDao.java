package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.manage_course.service.CourseBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseBaseService courseBaseService;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");

        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseBaseService.findById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }

    @Test
    public void testFindByPage(){
        CourseListRequest courseListRequest = new CourseListRequest();
        courseListRequest.setCompanyId("2");
        List<CourseInfo> list = courseBaseService.findByPage(0, 5, courseListRequest);
        Long count = courseBaseService.findCount(courseListRequest);
        list.forEach(courseBase ->{
            System.out.println(courseBase);
        });
    }


    @Test
    public void testSave(){
        CourseMarket courseMarket = new CourseMarket();
        courseMarket.setId("111111111");
        courseMarket.setCharge("测试");
        courseMarket.setStartTime(new Date());
        courseMarket.setEndTime(new Date());
        courseMarket.setQq("411111111111");
        courseMarket.setPrice(110.1);
        courseMarket.setPrice_old(10D);
        courseMarket.setValid("111111111");
        Long time = courseMarket.getEndTime().getTime() - courseMarket.getStartTime().getTime();
        courseMarketMapper.save(courseMarket);
    }

    @Test
    public void testUpate(){
        CourseMarket courseMarket = courseMarketMapper.findById("4028098171240c190171241492990000");

        courseMarket.setQq("666666666666");

        courseMarketMapper.update(courseMarket.getId(),courseMarket);
    }



}

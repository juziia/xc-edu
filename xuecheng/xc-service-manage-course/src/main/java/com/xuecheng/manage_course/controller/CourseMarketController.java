package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course/courseMarket")
public class CourseMarketController implements CourseMarketControllerApi {

    @Autowired
    private CourseMarketService courseMarkerService;


    @Override
    @PostMapping("add/{courseId}")
    public ResponseResult addCourseMarket(@PathVariable String courseId,
                                          @RequestBody CourseMarket courseMarket) {

        CourseMarket result = courseMarkerService.addCourseMarket(courseId, courseMarket);
        if(result == null){
            return ResponseResult.FAIL();
        }
        return ResponseResult.SUCCESS();
    }

    @Override
    @GetMapping("get/{courseId}")
    public CourseMarket findById(@PathVariable("courseId") String courseId) {

        return courseMarkerService.findById(courseId);
    }
}

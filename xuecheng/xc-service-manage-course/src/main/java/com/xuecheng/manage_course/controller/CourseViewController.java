package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseViewControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.service.CourseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course")
public class CourseViewController implements CourseViewControllerApi {

    @Autowired
    private CourseViewService courseViewService;

    @Override
    @GetMapping("detail/view/{id}")
    public CourseView courseview(@PathVariable("id") String courseId) {

        return courseViewService.courseView(courseId);
    }

    @Override
    @PostMapping("preview/{id}")
    public CoursePreviewResult preview(@PathVariable("id") String courseId) {
        String url = this.courseViewService.preview(courseId);
        if(url != null){
            return new CoursePreviewResult(CommonCode.SUCCESS,url);
        }
        return new CoursePreviewResult(CommonCode.FAIL,null);
    }

    @Override
    @PostMapping("/postPageQuick/{courseId}")
    public CoursePreviewResult publish(@PathVariable("courseId") String courseId) {
        String pageUrl = this.courseViewService.publish(courseId);
        return new CoursePreviewResult(CommonCode.SUCCESS,pageUrl);
    }
}

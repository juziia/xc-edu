package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.TeachPlanControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course/teachplan")
public class TeachPlanController implements TeachPlanControllerApi {

    @Autowired
    private TeachplanService teachplanService;


    @Override
    @GetMapping("list/{courseId}")
    public TeachplanNode findList(@PathVariable("courseId") String courseId) {


        return teachplanService.findList(courseId);
    }

    @Override
    @PostMapping("add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        teachplan = this.teachplanService.addTeachPlan(teachplan);
        if(teachplan == null){
            return new ResponseResult(CourseCode.COURSE_ADD_TEACHPLAN_ERROR);
        }
        return ResponseResult.SUCCESS();
    }

    @Override
    @PostMapping("savemedia")
    public ResponseResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        this.teachplanService.saveMedia(teachplanMedia);

        return new ResponseResult(CommonCode.SUCCESS);
    }
}

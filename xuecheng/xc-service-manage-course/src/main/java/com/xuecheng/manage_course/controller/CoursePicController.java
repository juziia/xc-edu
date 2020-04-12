package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePicControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CoursePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course/coursepic")
public class CoursePicController implements CoursePicControllerApi {

    @Autowired
    private CoursePicService coursePicService;

    @Override
    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("list/{courseId}")
    public CoursePic picList(@PathVariable("courseId") String courseId) {
        CoursePic coursePic = coursePicService.findPicList(courseId);
        return coursePic;
    }

    @Override
    @PostMapping("add")
    public ResponseResult addPic(@RequestParam("courseId") String courseId,
                                 @RequestParam("pic") String pic) {
        this.coursePicService.addPic(courseId,pic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @DeleteMapping("delete")
    @PreAuthorize("hasAuthority('delete_pid')")
    public ResponseResult deletePid(@RequestParam("courseId") String courseId) {
        this.coursePicService.deletePic(courseId);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}

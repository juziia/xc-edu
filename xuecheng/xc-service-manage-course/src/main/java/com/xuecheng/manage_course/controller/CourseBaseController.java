package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseBaseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("course/coursebase")
public class CourseBaseController extends BaseController implements CourseBaseControllerApi {

    @Autowired
    private CourseBaseService courseBaseService;


    @Override
    @GetMapping("list/{page}/{size}")
    public QueryResponseResult findCoursePage(@PathVariable("page") int page,
                                              @PathVariable("size") int size,
                                              CourseListRequest courseListRequest) {
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        String companyId = userJwt.getCompanyId();
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);

        List<CourseInfo> courseInfoList = courseBaseService.findByPage(page, size, courseListRequest);
        // 定义响应对象
        if(CollectionUtils.isEmpty(courseInfoList)){
            return new QueryResponseResult(CommonCode.SERVER_ERROR,null);
        }
        // 查询总记录数
        Long count = courseBaseService.findCount(courseListRequest);
        QueryResult<CourseInfo> queryResult = new QueryResult(courseInfoList,count);

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    @Override
    @PostMapping("add")
    public ResponseResult addCourse(@RequestBody CourseBase courseBase) {
        if(courseBase == null){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        courseBase.setId(null);
        this.courseBaseService.addCourseBase(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    @PutMapping("update/{courseId}")
    public ResponseResult updateCourse(@PathVariable("courseId") String courseId,@RequestBody CourseBase courseBase){
        courseBase = this.courseBaseService.updateCourseBase(courseId,courseBase);
        if(courseBase == null){
            return new ResponseResult(CourseCode.COURSE_IS_NOTEXIST);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }



    @Override
    @GetMapping("get/{courseId}")
    public CourseBase findById(@PathVariable("courseId") String courseId) {

        return courseBaseService.findById(courseId);
    }
}

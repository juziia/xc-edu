package com.xuecheng.api.course;

import com.xuecheng.api.cms.PageControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Api(value = "课程管理接口" ,description = "提供对课程的增删改查操作")
public interface CourseBaseControllerApi {

    /**
     *  分页查询课程
     */
    @ApiOperation("分页查询课程")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "size",value = "每页显示的记录数",required = true,dataType = "int",paramType = "path")
    })
    public QueryResponseResult findCoursePage(int page, int size, CourseListRequest courseListRequest);


    @ApiOperation("添加课程")
    public ResponseResult addCourse(CourseBase courseBase);


    @ApiOperation("根据课程Id查询课程信息")
    @ApiImplicitParam(name = "courseId",value = "课程Id",paramType = "path",dataType = "string",required = true)
    public CourseBase findById(String courseId);

    @ApiOperation("根据id修改课程基本信息")
    @ApiImplicitParam(name = "courseId",value = "课程id",required = true,dataType = "string",paramType = "path")
    public ResponseResult updateCourse( String courseId, CourseBase courseBase);

}

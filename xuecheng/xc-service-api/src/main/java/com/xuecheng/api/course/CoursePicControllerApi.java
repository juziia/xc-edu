package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程图片接口",description = "提供对课程图片的增删改查")
public interface CoursePicControllerApi {

    @ApiOperation("根据课程id查询课程图片")
    @ApiImplicitParam(name = "courseId",value = "课程id",required = true,paramType = "path",dataType = "string")
    public CoursePic picList(String courseId);

    @ApiOperation("添加课程图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId",value = "课程id",paramType = "query",required = true,dataType = "string"),
            @ApiImplicitParam(name = "pic",value = "图片id",paramType = "query",required = true,dataType = "string"),
    })
    public ResponseResult addPic(String courseId,String pic);

    @ApiOperation("根据课程id删除图片")
    @ApiImplicitParam(name = "courseId",value = "课程id",paramType = "query",required = true,dataType = "string")
    public ResponseResult deletePid(String courseId);

}

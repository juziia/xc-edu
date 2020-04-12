package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程计划接口",description = "提供对课程计划的增删改查")
public interface TeachPlanControllerApi {

    @ApiOperation("根据课程id查询课程计划")
    @ApiImplicitParam(name = "courseId",value = "课程id",paramType = "path",dataType = "string",required = true)
    public TeachplanNode findList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("保存课程计划与媒资文件的关联信息")
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia);

}

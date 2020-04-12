package com.xuecheng.api.course;


import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程营销接口",description = "提供课程营销的增删改查接口")
public interface CourseMarketControllerApi  {

    @ApiOperation("保存或修改课程营销数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId",value = "课程Id",required = true,dataType = "string",paramType = "path")})
    public ResponseResult addCourseMarket(String courseId, CourseMarket courseMarket);

    @ApiOperation("根据id查询课程营销信息")
    @ApiImplicitParam(name = "courseId",value = "课程id",paramType = "path",dataType = "string",required = true)
    public CourseMarket findById(String courseId);
}

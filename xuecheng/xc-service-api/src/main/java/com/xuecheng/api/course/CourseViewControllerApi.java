package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.portalview.PreViewCourse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程视图接口")
public interface CourseViewControllerApi {

    @ApiOperation("根据课程id查询课程详情视图模型数据")
    public CourseView courseview(String courseId);

    @ApiOperation("根据课程id对课程详情页面进行预览")
    public CoursePreviewResult preview(String courseId);

    @ApiOperation("一键发布")
    public CoursePreviewResult publish(String courseId);
}

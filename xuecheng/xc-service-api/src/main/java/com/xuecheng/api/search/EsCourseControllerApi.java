package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Api(value = "课程搜索接口",description = "提供对课程的综合查询",tags = "${课程搜索接口}")
public interface EsCourseControllerApi {

    @ApiOperation("课程综合查询")
     public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);


    @ApiOperation("课程列表查询")
    public Map<String,CoursePub> getById(String courseId);

    @ApiOperation("媒资信息查询")
    public TeachplanMediaPub getMedia(String teachplanId);


}

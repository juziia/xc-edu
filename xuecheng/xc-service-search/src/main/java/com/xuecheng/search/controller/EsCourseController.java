package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("search/course/")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    @GetMapping("list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,
                                               @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        QueryResult<CoursePub> list = esCourseService.list(page,size,courseSearchParam);
        QueryResponseResult<CoursePub> queryResponseResult
                = new QueryResponseResult<>(CommonCode.SUCCESS,list);
        return queryResponseResult;
        

    }


    @Override
    @GetMapping("/getall/{courseId}")
    public Map<String, CoursePub> getById(@PathVariable("courseId") String courseId) {
        return this.esCourseService.getById(courseId);
    }

    @GetMapping("/getmedia/{teachplanId}")
    public TeachplanMediaPub getMedia(@PathVariable("teachplanId") String teachplanId){
        String[] teachplanIdArr = new String[]{teachplanId};
        QueryResult<TeachplanMediaPub> queryResult = this.esCourseService.getMedia(teachplanIdArr);
        List<TeachplanMediaPub> list = queryResult.getList();
        if(CollectionUtils.isEmpty(list)){
            return new TeachplanMediaPub();
        }
        return list.get(0);
    }


}


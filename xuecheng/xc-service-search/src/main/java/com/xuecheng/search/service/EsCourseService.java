package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResult;

import java.util.List;
import java.util.Map;


public interface EsCourseService {
    QueryResult list(int page, int size, CourseSearchParam courseSearchParam);

    Map<String, CoursePub> getById(String id);

    QueryResult<TeachplanMediaPub> getMedia(String[] teachplanId);
}

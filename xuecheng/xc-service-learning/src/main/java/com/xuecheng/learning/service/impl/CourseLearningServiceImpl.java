package com.xuecheng.learning.service.impl;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import com.xuecheng.learning.service.CourseLearningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseLearningServiceImpl implements CourseLearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;


    @Override
    public String getMedia(String courseId, String teachplanId) {
        // 对用户信息进行校验

        if(StringUtils.isBlank(teachplanId)){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getMedia(teachplanId);

        return teachplanMediaPub.getMediaUrl();
    }
}

package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.domain.learning.response.LearningResponse;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.service.CourseLearningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    private CourseLearningService courseLearningService;




    @Override
    @GetMapping("getmedia/{courseId}/{teachplanId}")
    public LearningResponse getMedia(@PathVariable("courseId") String courseId,
                                     @PathVariable("teachplanId") String teachplanId) {
        String mediaUrl = courseLearningService.getMedia(courseId,teachplanId);
        if(StringUtils.isBlank(mediaUrl)){
            CastException.cast(LearningCode.LEARNING_MEDIA_ISBLANK);
        }

        return new LearningResponse(CommonCode.SUCCESS,mediaUrl);
    }
}

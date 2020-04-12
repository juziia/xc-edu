package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.LearningResponse;
import io.swagger.annotations.Api;

@Api("录播视频管理接口")
public interface CourseLearningControllerApi {

    LearningResponse getMedia(String courseId, String teachplanId);
}

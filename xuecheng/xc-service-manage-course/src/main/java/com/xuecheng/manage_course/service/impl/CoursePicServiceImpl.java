package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CoursePicMapper;
import com.xuecheng.manage_course.service.CoursePicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursePicServiceImpl implements CoursePicService {

    @Autowired
    private CoursePicMapper coursePicMapper;

    @Override
    public CoursePic findPicList(String courseId) {
        if(StringUtils.isBlank(courseId)){
            CastException.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        return coursePicMapper.findByCourseId(courseId);
    }

    @Override
    public void addPic(String courseId, String pic) {
        CoursePic coursePic = this.coursePicMapper.findByCourseId(courseId);
        if(coursePic == null){
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        int result = this.coursePicMapper.addPic(courseId,pic);
        if(result <= 0){
            CastException.cast(CommonCode.FAIL);
        }
    }

    @Override
    public void deletePic(String courseId) {
        int result = this.coursePicMapper.deletePic(courseId);
        if(result <= 0 ){
            CastException.cast(CommonCode.FAIL);
        }
    }
}

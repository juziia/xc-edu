package com.xuecheng.manage_course.service.impl;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.service.CourseBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseBaseServiceImpl implements CourseBaseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseBaseRepository courseBaseRepository;



    public List<CourseInfo> findByPage(int page, int size, CourseListRequest courseListRequest){
        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 20;
        }

        PageHelper.startPage(page,size);

        return courseMapper.selectList(courseListRequest);
    }

    /**
     *  根据查询参数查询总记录数
     * @param courseListRequest
     * @return
     */
    public Long findCount(CourseListRequest courseListRequest){
        return courseMapper.totalCount(courseListRequest);
    }

    @Override
    @Transactional
    public void addCourseBase(CourseBase courseBase) {
        if(courseBase == null){
            // 抛出异常
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        courseBaseRepository.save(courseBase);
    }

    /**
     *  根据id查询课程基本信息
     * @param courseId
     * @return
     */
    @Override
    public CourseBase findById(String courseId) {
        if(StringUtils.isBlank(courseId)){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public CourseBase updateCourseBase(String courseId, CourseBase courseBase) {
        if(StringUtils.isBlank(courseId)){
            return null;
        }
        // 根据课程id查询课程基本信息
        CourseBase courseBaseResult = this.findById(courseId);
        // 判断是否为null
        if(courseBase == null){
            return null;
        }
        courseBase.setId(courseId);

        return this.courseBaseRepository.save(courseBase);

    }

    @Override
    @Transactional
    @Rollback(false)
    public void updateCourseBaseStatus(String id ,String status) {

        this.courseBaseRepository.updateStatus(id,status);
    }


}

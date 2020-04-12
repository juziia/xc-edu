package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.CourseMarketMapper;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseMarketServiceImpl implements CourseMarketService {

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseBaseService courseBaseService;

    @Transactional
    public CourseMarket addCourseMarket(String courseId, CourseMarket courseMarket) {
        // 判断id是否为null 或者为空
        if (StringUtils.isBlank(courseId)) {
            CastException.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        // 根据课程id查询CourseBase对象
        CourseBase courseBase = courseBaseService.findById(courseId);
        if (courseBase == null) {
            CastException.cast(CourseCode.COURSE_IS_NOTEXIST);
        }

        CourseMarket courseMarketResult = courseMarketMapper.findById(courseId);
        if (courseMarketResult == null) {
            // 如果不存在就添加
            courseMarket.setId(courseId);
            // 保存
            courseMarketMapper.save(courseMarket);
        } else {
            // 存在就修改
            courseMarketMapper.update(courseId, courseMarket);

        }
        // 返回课程营销对象
        return courseMarket;
    }

    /*@Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.findById(id);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketMapper.save(one);
            return one;
        }else{
            //添加课程营销信息
            one   =   new   CourseMarket();
            BeanUtils.copyProperties(courseMarket,   one);
            //设置课程id
            one.setId(id);
            courseMarketMapper.save(one);
        }
        return one;
    }*/

    @Override
    public CourseMarket findById(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            return null;
        }

        return courseMarketMapper.findById(courseId);
    }


}

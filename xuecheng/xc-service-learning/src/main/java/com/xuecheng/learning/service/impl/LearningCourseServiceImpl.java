package com.xuecheng.learning.service.impl;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.dao.LearningCourseRepository;
import com.xuecheng.learning.dao.TaskHisRepository;
import com.xuecheng.learning.service.LearningCourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class LearningCourseServiceImpl implements LearningCourseService {

    @Autowired
    private LearningCourseRepository learningCourseRepository;
    @Autowired
    private TaskHisRepository taskHisRepository;

    @Override
    @Transactional
    public XcTaskHis addLearningCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(courseId)){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }

        if(xcTask == null || StringUtils.isBlank(xcTask.getId())){
            CastException.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }


        // 根据用户id和课程id查询 课程选课信息
        XcLearningCourse xcLearningCourse = this.learningCourseRepository.findByUserIdAndCourseId(userId, courseId);
        // 判断是否存在,存在即更改信息,不存在就插入数据
        if(xcLearningCourse != null){
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setValid(valid);
            // 保存到数据库
            learningCourseRepository.save(xcLearningCourse);
        }else{
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("50101");    // 50101 选课状态正常
            learningCourseRepository.save(xcLearningCourse);
        }

        // 添加选课完成

        // 将任务添加到历史任务表中
        Optional<XcTaskHis> optional = taskHisRepository.findById(xcTask.getId());
        // 任务不存在
        if(optional.isPresent()){
           return optional.get();
        }
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask,xcTaskHis);
        // 添加完成时间
        xcTaskHis.setDeleteTime(new Date());

        return taskHisRepository.save(xcTaskHis);
    }
}

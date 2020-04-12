package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.TeachPlanMediaRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.xuecheng.manage_course.service.TeachplanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private TeachPlanMediaRepository teachplanMediaRepository;

    @Override
    public TeachplanNode findList(String courseId) {
        if(StringUtils.isBlank(courseId)){
            return null;
        }

        return teachplanMapper.findList(courseId);
    }


    @Override
    public Teachplan addTeachPlan(Teachplan teachplan) {
        // 根据课程id查询课程计划
        // 获取课程计划id
        String courseId = teachplan.getCourseid();
        // 获取父节点id
        String parentId = teachplan.getParentid();
        // 判断父节点是否为null或为空
        if(StringUtils.isBlank(parentId)){
            // 根据课程id和父节点查询课程计划对象,由于父节点为null or empty 所以父节点就为 0
            List<Teachplan> parentTeachplanList = this.teachplanMapper.findByCourseIdAndParentId(courseId, "0");
            // 判断父课程计划是否为null
            if(parentTeachplanList == null || parentTeachplanList.isEmpty()){

                return saveParentToRoot(teachplan,courseId);
            }else{
                // 获取根课程
                Teachplan parentTeachplan = parentTeachplanList.get(0);
                // 获取根节点id设置为父节点的父id
                teachplan.setParentid(parentTeachplan.getId());
                // 设置等级,父节点为2
                // teachplan.setGrade("2");
                this.setGrade(teachplan,parentTeachplan.getGrade());

                return this.teachplanRepository.save(teachplan);
            }

        }
        // 父节点id不为空,则根据父节点id查询父课程对象信息
        Teachplan parentTeachplan = teachplanMapper.finById(teachplan.getParentid());
        if(parentTeachplan == null){
            return null;
        }
//        teachplan.setGrade("3");
        this.setGrade(teachplan,parentTeachplan.getGrade());

        return teachplanRepository.save(teachplan);
    }

    @Override
    public void saveMedia(TeachplanMedia teachplanMedia) {
        // 判断是否为null
        if(teachplanMedia == null || StringUtils.isBlank(teachplanMedia.getTeachplanId())){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }

        // 获取课程计划id
        String teachplanId = teachplanMedia.getTeachplanId();
        // 根据课程计划id获取课程计划信息
        Teachplan teachplan = this.findById(teachplanId);
        // 判断课程计划级别是否为3
        if(! StringUtils.equals("3",teachplan.getGrade())){
            CastException.cast(CourseCode.COURSE_TEACHPLAN_MEDIA_GRADERROR);
        }
        // 根据课程计划id查询课程计划与媒资文件的关联信息
        Optional<TeachplanMedia> optional = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia newTeachplanMedia = null;
        if(optional.isPresent()){
            newTeachplanMedia = optional.get();
        }else{
            newTeachplanMedia = new TeachplanMedia();
        }
        newTeachplanMedia.setCourseId(teachplan.getCourseid());  // 课程id
        newTeachplanMedia.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());  // 媒资文件原始名称
        newTeachplanMedia.setMediaId(teachplanMedia.getMediaId());      // 媒资id
        newTeachplanMedia.setTeachplanId(teachplanId);      // 课程计划id
        newTeachplanMedia.setMediaUrl(teachplanMedia.getMediaUrl());    // 媒资文件url

        teachplanMediaRepository.save(newTeachplanMedia);
    }

    public Teachplan findById(String teachplanId){
        Optional<Teachplan> optional = this.teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        return optional.get();
    }



    /**
     *  课程关系为 根 --> 父 --> 子
     *      根据课程id获取课程名称,创建根课程,将传入的课程对象作为父课程添加到数据库
     */
    private Teachplan saveParentToRoot(Teachplan teachplan,String courseId){
        // 课程关系为 :  根 --> 父 --> 子
        // 为null,则添加根课程计划,再将传入的课程计划作为父课程计划添加至数据库中
        // 查询课程基本信息
        CourseBase courseBase = courseBaseService.findById(courseId);
        if(courseBase == null){
            return null;
        }
        // 获取课程信息名称
        String courseBaseName = courseBase.getName();
        // 创建一个根课程计划对象,存数数据库中
        Teachplan rootTeachplan = new Teachplan();

        rootTeachplan.setPname(courseBaseName);
        rootTeachplan.setParentid("0");
        rootTeachplan.setGrade("1");
        rootTeachplan.setCourseid(courseId);
        rootTeachplan.setOrderby(1);
        rootTeachplan.setStatus("0");

        rootTeachplan = this.teachplanRepository.save(rootTeachplan);
        // 获取根课程计划的id
        String rootId = rootTeachplan.getId();
        // 设置父节点id
        teachplan.setParentid(rootId);
//        teachplan.setGrade("2");
        this.setGrade(teachplan,rootTeachplan.getGrade());
        Teachplan parentTeachplan = this.teachplanRepository.save(teachplan);
        return parentTeachplan;
    }

    private void setGrade(Teachplan teachplan, String grade) {
        if("1".equals(grade)){
            teachplan.setGrade("2");

        }else if("2".equals(grade)){
            teachplan.setGrade("3");
        }
    }

}

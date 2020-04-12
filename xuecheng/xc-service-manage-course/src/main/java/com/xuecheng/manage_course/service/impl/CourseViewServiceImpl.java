package com.xuecheng.manage_course.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlUrlResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.config.CoursePublishProperties;
import com.xuecheng.manage_course.dao.CoursePubRepository;
import com.xuecheng.manage_course.dao.TeachPlanMediaRepository;
import com.xuecheng.manage_course.dao.TeachplanMediaPubRepository;
import com.xuecheng.manage_course.service.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(CoursePublishProperties.class)
public class CourseViewServiceImpl implements CourseViewService {


    private static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CoursePublishProperties coursePublishProperties;

    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private CoursePicService coursePicService;
    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    TeachplanMediaPubRepository teachPlanMediaPubRepository;

    @Autowired
    TeachPlanMediaRepository teachPlanMediaRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CoursePubRepository coursePubRepository;

    @Override
    public CourseView courseView(String courseId) {
        CourseView courseView = new CourseView();
        courseView.setCourseBase(courseBaseService.findById(courseId));
        courseView.setCourseMarket(courseMarketService.findById(courseId));
        courseView.setCoursePic(coursePicService.findPicList(courseId));
        courseView.setTeachplanNode(teachplanService.findList(courseId));

        return courseView;
    }

    @Override
    public String preview(String courseId) {
        //根据课程id查询课程对象
        CourseBase courseBase = courseBaseService.findById(courseId);
        if (courseBase == null) {
            CastException.cast(CourseCode.COURSE_IS_NOTEXIST);
        }
        String courseName = courseBase.getName();

        CmsPage cmsPage = this.getCmsPage(courseId, courseName);


        CmsPageResult cmsPageResult = cmsPageClient.addCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            //保存失败,抛出异常
            CastException.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        CmsPage result = cmsPageResult.getCmsPage();

        return coursePublishProperties.getPreviewUrl() + result.getPageId() + ".html";
    }


    @Override
    @Transactional
    public String publish(String courseId) {
        // 课程服务拼装CmsPage信息,调用Cms服务进行保存发布
        if (StringUtils.isBlank(courseId)) {
            CastException.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        // 根据课程id查询课程基本信息
        CourseBase courseBase = courseBaseService.findById(courseId);
        // 组装CmsPage对象
        CmsPage cmsPage = this.getCmsPage(courseId, courseBase.getName());
        // 远程调用cms服务提供的一键发布接口
        GenerateHtmlUrlResult generateHtmlUrlResult = cmsPageClient.publishCoursePage(cmsPage);
        // 判断是否响应成功
        if (!generateHtmlUrlResult.isSuccess()) {
            CastException.cast(CommonCode.FAIL);
        }
        // 获取url
        String pageUrl = generateHtmlUrlResult.getPageUrl();
        // 修改页面状态为已发布 202002
        courseBase.setStatus("202002");
        this.courseBaseService.updateCourseBaseStatus(courseBase.getId(), courseBase.getStatus());

        // 保存索引信息
        CoursePub coursePub = this.createCoursePub(courseBase.getId());
        this.saveCoursePub(courseId, coursePub);

        // 保存课程计划与媒资文件的索引信息
        this.saveTeachPlanMediaRepository(courseId);


        // 返回页面url
        return pageUrl;
    }

    private void saveTeachPlanMediaRepository(String courseId) {
        // 根据课程id,将所有关于课程id的信息删除
        this.teachPlanMediaPubRepository.deleteByCourseId(courseId);
        // 根据课程id查询课程计划与媒资文件的关联数据
        List<TeachplanMedia> teachplanMediaList = teachPlanMediaRepository.findByCourseId(courseId);
        // 将TeachplanMedia对象转换为TeachplanMediaPun对象
        List<TeachplanMediaPub> teachplanMediaPubList = teachplanMediaList.stream().map(teachplanMedia -> {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            return teachplanMediaPub;
        }).collect(Collectors.toList());

        teachPlanMediaPubRepository.saveAll(teachplanMediaPubList);

    }

    private void saveCoursePub(String courseId, CoursePub coursePub) {
        coursePub.setId(courseId);
        this.coursePubRepository.save(coursePub);

    }


    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        // 设置课程基本信息
        CourseBase courseBase = courseBaseService.findById(id);
        BeanUtils.copyProperties(courseBase, coursePub);
        CoursePic coursePic = coursePicService.findPicList(id);
        BeanUtils.copyProperties(coursePic, coursePub);
        CourseMarket courseMarket = courseMarketService.findById(id);
        BeanUtils.copyProperties(courseMarket, coursePub);
        TeachplanNode teachplanNode = teachplanService.findList(id);
        String teachplanNodeJson = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanNodeJson);
        coursePub.setTimestamp(new Date());
        String pubTime = SIMPLEDATEFORMAT.format(coursePub.getTimestamp());
        coursePub.setPubTime(pubTime);

        return coursePub;
    }

    @NotNull
    private CmsPage getCmsPage(String courseId, String courseName) {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseName);
        cmsPage.setPageCreateTime(new Date());
        cmsPage.setDataUrl(coursePublishProperties.getDataUrl() + courseId);
        cmsPage.setSiteId(coursePublishProperties.getSiteId());
        cmsPage.setTemplateId(coursePublishProperties.getTemplateId());
        cmsPage.setPagePhysicalPath(coursePublishProperties.getPagePhysicalPath());
        cmsPage.setPageWebPath(coursePublishProperties.getPageWebPath());
        cmsPage.setPageType(coursePublishProperties.getPageType());
        return cmsPage;
    }

}

package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;

public interface TeachplanService {

    TeachplanNode findList(String courseId);

    Teachplan addTeachPlan(Teachplan teachplan);

    void saveMedia(TeachplanMedia teachplanMedia);
}

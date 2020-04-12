package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator.
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {

    // 使用Spring data jpa 实现修改更行删除操作,可以使用Query注解来配置sql语句,但是需要和Modifying直接搭配使用
    // 其二: 参数使用 ?作为占位符,使用下标来注明参数在sql语句中的位置,下标从1开始,实例: ?1
    @Query(value = "update CourseBase set status = ?2 where id = ?1 ")
    @Modifying  // 声明是一个修改操作
    public void updateStatus(String id,String status);
}

    
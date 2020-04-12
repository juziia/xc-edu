package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<XcTask,String>{

    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date date);

    @Modifying
    @Query("update XcTask set updateTime = ?2 where id = ?1")
    int updateTaskUpdateTime(String taskId,Date updateTime);


    @Modifying
    @Query("update XcTask set version = :version + 1 where id = :taskId and version = :version ")
    int updateTaskVersion(@Param("taskId") String taskId,@Param("version") int version);
}

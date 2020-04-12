package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CoursePicMapper coursePicMapper;


    @Test
    public void testFindList(){
        CategoryNode categoryNode = categoryMapper.findList();
        System.out.println(categoryNode);
    }

    @Test
    public void testInsert(){
        int result = coursePicMapper.addPic("11111111", "11111111111");
        int result2 = coursePicMapper.deletePic("11111111");
        System.out.println(result);
    }



}

package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindByPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void findAllByExample(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("轮播");

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 对pageAliase属性进行包含查询
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Pageable pageable = PageRequest.of(0,10);
        Page<CmsPage> pageList = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = pageList.getContent();
        System.out.println(content);
    }


}

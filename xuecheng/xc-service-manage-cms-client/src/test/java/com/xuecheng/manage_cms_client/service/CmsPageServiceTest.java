package com.xuecheng.manage_cms_client.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageServiceTest {

    @Autowired
    private PageService pageService;

    @Test
    public void testSavePageToServerPath(){
        pageService.savePageToServerPath("5e7d6c287ec26d4b841f9f1c");
    }

}

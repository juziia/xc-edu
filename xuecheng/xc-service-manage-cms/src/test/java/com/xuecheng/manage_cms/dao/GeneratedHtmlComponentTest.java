package com.xuecheng.manage_cms.dao;

import com.xuecheng.CmsManageApplication;
import com.xuecheng.manage_cms.component.GeneratedHtmlComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CmsManageApplication.class)
@RunWith(SpringRunner.class)
public class GeneratedHtmlComponentTest {

    @Autowired
    private GeneratedHtmlComponent generatedHtmlComponent;

    @Test
    public void testBuildHtml(){
        String html = generatedHtmlComponent.buildHtml("5e7d6c287ec26d4b841f9f1c");
    }

}

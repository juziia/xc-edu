package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.component.GeneratedHtmlComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("cms/preview")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private GeneratedHtmlComponent generatedHtmlComponent;


    @GetMapping("{pageId}.html")
    public void preview(@PathVariable("pageId")String pageId){
        // 根据pageId获取页面静态化的内容
        String htmlContent = generatedHtmlComponent.buildHtml(pageId);

        try {
            // 将响应类型设置为html类型
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

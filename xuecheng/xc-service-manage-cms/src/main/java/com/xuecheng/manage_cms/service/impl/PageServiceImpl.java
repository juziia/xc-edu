package com.xuecheng.manage_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.manage_cms.component.GeneratedHtmlComponent;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.service.CmsPageService;
import com.xuecheng.manage_cms.service.CmsSiteService;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    private static Map<String,String> MESSAGE_MAP = new HashMap<>();

    @Autowired
    private GeneratedHtmlComponent generatedHtmlComponent;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CmsPageService cmsPageService;

    @Autowired
    private CmsSiteService cmsSiteService;





    @Override
    public void postPage(String pageId) {
        // 根据页面id获取页面静态化的内容
        String html = generatedHtmlComponent.buildHtml(pageId);

        // 将页面静态化内容存入gridFS中
        CmsPage cmsPage = this.saveHtmlToGridFs(html, pageId);

        // 发送信息到rabbitmq中
        MESSAGE_MAP.put("pageId",pageId);
        String messageJsonStr = JSON.toJSONString(MESSAGE_MAP);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,cmsPage.getSiteId(),messageJsonStr);

    }

    @Override
    public String publish(CmsPage cmsPage) {
        // 将cmsPae进行保存
        CmsPage cmsPageResult = this.cmsPageService.addCmsPage(cmsPage);
        // 调用发布接口
        this.postPage(cmsPageResult.getPageId());
        String siteId = cmsPageResult.getSiteId();
        CmsSite cmsSite = cmsSiteService.findById(siteId);

        //页面Url= cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName
        String pageUrl = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPageResult.getPageWebPath() + cmsPageResult.getPageName();

        return pageUrl;
    }

    private CmsPage saveHtmlToGridFs(String html, String pageId) {
        // 将html内容转换为输入流
        InputStream input = null;
        // 根据页面id查询页面信息
        CmsPage cmsPage = cmsPageService.findById(pageId);
        if(cmsPage == null){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISIT);
        }
        try {
            input = IOUtils.toInputStream(html, "utf-8");
            // 保存到gridFs中
            ObjectId id = gridFsTemplate.store(input, cmsPage.getPageName());
            // 将文件id存储到cmsPage对象中
            cmsPage.setHtmlFileId(id.toString());
            // 更新cmsPage
            cmsPageService.save(cmsPage);
            return cmsPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

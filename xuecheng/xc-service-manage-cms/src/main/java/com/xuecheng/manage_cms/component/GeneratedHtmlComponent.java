package com.xuecheng.manage_cms.component;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPageService;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 *  实现页面静态化
 */
@Component
public class GeneratedHtmlComponent {

    @Autowired
    private CmsPageService cmsPageService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateService cmsTemplateService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;



    /**
     * 页面静态化
     */
    public String buildHtml(String pageId){

        // 根据页面id获取页面对象
        CmsPage cmsPage = cmsPageService.findById(pageId);
        if(cmsPage == null){
            CastException.cast(CmsCode.CMS_PAGE_NOTEXISIT);
        }

        // 根据dataUrl获取数据模型
        CourseView model = this.getDataModel(cmsPage.getDataUrl());
        if(model == null){
            CastException.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //  根据页面模板id获取页面模板内容
        CmsTemplate cmsTemplate = this.getTemplate(cmsPage.getTemplateId());
        if(cmsTemplate == null){
            CastException.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // 根据模板文件id获取页面模板内容
        String templateContent = this.getTemplateContent(cmsTemplate.getTemplateFileId());
        if(StringUtils.isBlank(templateContent)){
            CastException.cast(CmsCode.CMS_GENERATEHTML_TEMPLATECONTENT);
        }


        // 根据数据模型和页面模板内容实现页面静态化
        String htmlContent = this.generatedHtml(templateContent,model);
        if(StringUtils.isBlank(htmlContent)){
            CastException.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        return htmlContent;
    }


    private String generatedHtml(String templateContent, Object model) {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("utf-8");
        // 定义模板加载器
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("template",templateContent);
        // 配置模板加载器
        configuration.setTemplateLoader(templateLoader);
        // 获取模板对象
        try {
            Template template = configuration.getTemplate("template");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTemplateContent(String templateFileId) {
        // 获取文件id
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        // 根据文件id打开流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 根据流对象和模板文件id获取模板文件源对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

        try {
            // 获取模板内容
            return IOUtils.toString(gridFsResource.getInputStream(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private CmsTemplate getTemplate(String templateId) {
        return cmsTemplateService.findById(templateId);
    }

    private CourseView getDataModel(String dataUrl) {
        // 根据dataUrl获取模型
        ResponseEntity<CourseView> responseEntity = restTemplate.getForEntity(dataUrl, CourseView.class);

        return responseEntity.getBody();
    }


}

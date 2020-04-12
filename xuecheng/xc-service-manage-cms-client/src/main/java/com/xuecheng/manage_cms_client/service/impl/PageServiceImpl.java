package com.xuecheng.manage_cms_client.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    private Logger LOGGER = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;


    @Override
    public void savePageToServerPath(String pageId) {
        // 根据pageId获取CmsPage对象
        CmsPage cmsPage = this.findCmsPageByPageId(pageId);
        if(cmsPage == null){
            LOGGER.error("com.xuecheng.manage_cms_client.service.impl.PageServiceImpl.savePageToServerPath错误 cmsPage is null pageId: {}",pageId);
            return;
        }
        // 获取html文件id
        String htmlFileId = cmsPage.getHtmlFileId();
        // 根据htmlFileId获取文件读取流对象
        InputStream input = this.getFileIdByHtmlFileId(htmlFileId);
        if(input == null){
            LOGGER.info("根据htmlFileId获取文件读取流对象失败,htmlFileId:  {}",htmlFileId);
            return;
        }
        // 根据站点id获取站点服务器路径
        String serverPath = this.getServerPathBySiteId(cmsPage.getSiteId());
        // 页面物理路径=站点物理路径+页面物理路径+页面名称
        String pathPhysicalPath = serverPath + cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pathPhysicalPath);
            IOUtils.copy(input,output);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getServerPathBySiteId(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite.getSitePhysicalPath();
        }
        return null;
    }


    private InputStream getFileIdByHtmlFileId(String htmlFileId) {
        // 根据htmlFileId获取文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        // 根据文件对象id打开流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 创建源对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CmsPage findCmsPageByPageId(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }




}

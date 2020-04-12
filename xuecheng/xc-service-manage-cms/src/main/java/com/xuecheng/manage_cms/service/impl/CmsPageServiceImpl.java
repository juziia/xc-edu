package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsPageServiceImpl implements CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Override
    public Page<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest) {

        if (page <= 0) {
            page = 1;
        }
        // 页码从0开始,但为了调用接口方便,进行了业务处理
        page = page - 1;
        if (size <= 0) {
            //默认显示10条记录
            size = 10;
        }

        // 条件查询
        CmsPage cmsPage = new CmsPage();
        if (queryPageRequest != null) {
            if (StringUtils.isNotBlank(queryPageRequest.getPageAliase())) {
                cmsPage.setPageAliase(queryPageRequest.getPageAliase());
            }
            if (StringUtils.isNotBlank(queryPageRequest.getTemplateId())) {
                cmsPage.setTemplateId(queryPageRequest.getTemplateId());
            }
            if (StringUtils.isNotBlank(queryPageRequest.getPageId())) {
                cmsPage.setPageId(queryPageRequest.getPageId());
            }
            if (StringUtils.isNotBlank(queryPageRequest.getSiteId())) {
                cmsPage.setSiteId(queryPageRequest.getSiteId());
            }
            if (StringUtils.isNotBlank(queryPageRequest.getPageName())) {
                cmsPage.setPageName(queryPageRequest.getPageName());
            }
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);


        Pageable pageable = PageRequest.of(page, size);
        // 进行分页查询
        Page<CmsPage> pageResult = cmsPageRepository.findAll(example, pageable);


        return pageResult;
    }

    @Override
    public CmsPage addCmsPage(CmsPage cmsPage) {
        if (cmsPage == null) {
            return null;
        }

        // 根据cmsPage的主键字段进行查询,判断是否已经存在
        CmsPage result = this.cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if(result != null){
            return this.update(result.getPageId(), cmsPage);
        }


        // 不存在
        cmsPage.setPageId(null);
        return this.cmsPageRepository.save(cmsPage);
    }

    @Override
    public CmsPage findById(String id) {
        Optional<CmsPage> optional = this.cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        this.cmsPageRepository.deleteById(id);
    }

    @Override
    public CmsPage update(String id, CmsPage cmsPage) {
        // 判断cmsPage是否为null
        if (cmsPage == null) {
            return null;
        }

        // 根据id进行查询
        CmsPage cmsPageResult = this.findById(id);

        if (cmsPageResult != null) {
            cmsPageResult.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cmsPageResult.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPageResult.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPageResult.setPageName(cmsPage.getPageName());
            //更新访问路径
             cmsPageResult.setPageWebPath(cmsPage.getPageWebPath()); 
             //更新物理路径 
             cmsPageResult.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
             cmsPageResult.setDataUrl(cmsPage.getDataUrl());
             this.cmsPageRepository.save(cmsPageResult);
             return cmsPageResult;
        }
        return null;
    }

    @Override
    public void save(CmsPage cmsPage) {
        cmsPageRepository.save(cmsPage);
    }
}

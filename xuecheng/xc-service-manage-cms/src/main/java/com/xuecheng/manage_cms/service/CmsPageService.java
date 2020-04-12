package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CmsPageService {

    /**
     *  分页查询
     * @param page  页数  从1开始
     * @param size  每页显示的记录数
     * @param queryPageRequest  查询请求参数
     * @return
     */
    Page<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest);

    CmsPage addCmsPage(CmsPage cmsPage);

    CmsPage findById(String id);

    void delete(String id);

    CmsPage update(String id, CmsPage cmsPage);

    void save(CmsPage cmsPage);
}

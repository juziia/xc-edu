package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;

public interface PageService {

    public void postPage(String pageId);

    String publish(CmsPage cmsPage);
}

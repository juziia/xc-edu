package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *  操作Mongodb中的cms_page集合的dao接口
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);

}

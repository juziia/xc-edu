package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlUrlResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  value: 指定远程调用的微服务名称
 *  contextId: 指定当前接口在spring容器中的bean名称
 */
@FeignClient(value="xc-service-manage-cms",contextId= "cmsPageClient")
@RequestMapping("cms")
public interface CmsPageClient {

    @PostMapping("/page/add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage);

    @PostMapping("page/publish")
    GenerateHtmlUrlResult publishCoursePage(@RequestBody CmsPage cmsPage);
}

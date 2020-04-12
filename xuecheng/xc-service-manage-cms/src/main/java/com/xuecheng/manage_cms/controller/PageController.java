package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.PageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlUrlResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.component.GeneratedHtmlComponent;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class PageController implements PageControllerApi {

    @Autowired
    private PageService pageService;

    @Override
    @PostMapping("postPage/{pageId}")
    public ResponseResult postPage(@PathVariable("pageId") String pageId) {
        pageService.postPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/page/publish")
    public  GenerateHtmlUrlResult publish(@RequestBody CmsPage cmsPage) {
        String pageUrl = this.pageService.publish(cmsPage);

        return new GenerateHtmlUrlResult(CommonCode.SUCCESS,pageUrl);
    }
}

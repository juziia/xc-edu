package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest request) {
        Page<CmsPage> pageResult = cmsPageService.findList(page, size, request);

        // 进行封装结果集
        QueryResult queryResult = new QueryResult<CmsPage>();
        queryResult.setList(pageResult.getContent());
        queryResult.setTotal(pageResult.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;
    }

    @Override
    @PostMapping("/page/add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage) {
        cmsPage = this.cmsPageService.addCmsPage(cmsPage);
        if(cmsPage != null){
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    @Override
    @GetMapping("page/get/{id}")
    public CmsPageResult findById(@PathVariable("id") String id) {
        CmsPage cmsPage = this.cmsPageService.findById(id);
        if(cmsPage != null){
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    @Override
    @PutMapping("page/update/{id}")
    public CmsPageResult updateCmsPage(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        cmsPage = this.cmsPageService.update(id,cmsPage);
        if(cmsPage != null){
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    @Override
    @DeleteMapping("page/delete/{id}")
    public CmsPageResult deleteCmsPage(@PathVariable("id") String id) {
        this.cmsPageService.delete(id);
        return new CmsPageResult(CommonCode.SUCCESS,null);
    }




}

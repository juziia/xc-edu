package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cms")
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService cmsSiteService;

    @GetMapping("site/list")
    public QueryResponseResult findAll() {
        QueryResult<CmsSite> queryResult = new QueryResult<>();
        List<CmsSite> siteList = cmsSiteService.findAll();
        queryResult.setList(siteList);
        queryResult.setTotal(siteList.size());

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}

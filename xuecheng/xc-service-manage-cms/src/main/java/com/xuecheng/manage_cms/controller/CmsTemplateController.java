package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cms")
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    private CmsTemplateService cmsTemplateService;


    @Override
    @GetMapping("template/findAll")
    public QueryResponseResult findAll() {
        List<CmsTemplate> templateList = cmsTemplateService.findAll();
        QueryResult<CmsTemplate> queryResult = new QueryResult<>();
        queryResult.setList(templateList);
        queryResult.setTotal(queryResult.getTotal());

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}

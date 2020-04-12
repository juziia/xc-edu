package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面模板管理接口",description = "cms页面模板管理接口,对页面模板的增删改查")
public interface CmsTemplateControllerApi {

    @ApiOperation("查询所有页面模板数据")
    QueryResponseResult findAll();

}

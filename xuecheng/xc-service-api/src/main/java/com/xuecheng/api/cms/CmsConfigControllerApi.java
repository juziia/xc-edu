package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "CMS页面配置接口",description = "提供对页面配置模型数据的查询接口,修改接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询页面配置的模型数据")
    @ApiImplicitParam(name = "id",value = "页面配置的主键",required = true,dataType = "string",paramType = "path")
    CmsConfig findById(String id);
}

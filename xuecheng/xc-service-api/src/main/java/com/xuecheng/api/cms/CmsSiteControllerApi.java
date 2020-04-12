package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "cms页面站点管理接口",description = "页面资源管理站点接口,提供站点的增删改查方法")
public interface CmsSiteControllerApi {

    @ApiOperation(value = "查询所有站点信息")
    QueryResponseResult findAll();
}

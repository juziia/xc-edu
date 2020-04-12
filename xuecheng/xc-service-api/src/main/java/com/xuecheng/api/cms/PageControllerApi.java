package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlUrlResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

@Api(value = "页面发布与管理接口",description = "提供页面的发布,管理等接口")
public interface PageControllerApi {

    @ApiOperation("发布页面")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "pageId", value = "页面Id", required = true)
    public ResponseResult postPage(String pageId);

    @ApiOperation("一键发布")
    GenerateHtmlUrlResult publish(CmsPage cmsPage);

}

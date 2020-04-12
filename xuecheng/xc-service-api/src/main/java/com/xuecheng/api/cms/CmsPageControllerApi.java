package com.xuecheng.api.cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.*;

@Api(value = "cms页面管理接口", description = "cms页面管理接口,提供对页面的增删改查操作")
public interface CmsPageControllerApi {

    /**
     * 分页查询
     *
     * @param page    当前页数
     * @param size    每页显示的记录数
     * @param request
     * @return
     */
    @ApiOperation("分页查询页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest request);


    @ApiOperation("添加cms页面")
    @ApiParam(required = true,name = "cmsPage",value = "页面的数据模型对象")
    CmsPageResult addCmsPage(CmsPage cmsPage);


    @ApiOperation("根据id查询cms页面")
    @ApiImplicitParam(name = "id",value = "页面id",required = true,dataType = "string" ,paramType = "path")
    CmsPageResult findById(String id);


    @ApiOperation("修改页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "页面id",required = true,dataType = "string",paramType = "path")
    })
    @ApiParam(required = true,name = "cmsPage",value = "页面的数据模型对象")
    CmsPageResult updateCmsPage(String id,CmsPage cmsPage);

    @ApiOperation("根据id删除页面")
    @ApiImplicitParam(name = "id",value = "页面id",paramType = "path",dataType = "string",required = true)
    CmsPageResult deleteCmsPage(String id);
}

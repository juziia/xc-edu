package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "媒资文件管理接口",description = "提供对媒资文件的查询,删除,修改等功能")
public interface MediaFileControllerApi {


    @ApiOperation("分页查询媒资文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page",required = true,value = "页码",dataType = "int",paramType = "path"),
            @ApiImplicitParam(name = "size",required = true,value = "每页记录数",dataType = "int",paramType = "path")
    })
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);


}

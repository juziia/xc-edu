package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典接口",description = "提供对数据字典的增删改查接口")
public interface SysDictionaryControllerApi {

    @ApiOperation("根据类型查询数据字典")
    @ApiImplicitParam(name = "type",value = "数据字典类型",paramType = "path",dataType = "string",required = true)
    SysDictionary getByType(String type);


}

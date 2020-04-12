package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户管理接口",description = "用户管理接口")
public interface UserControllerApi {

    @ApiOperation("根据账号查询用户信息")
    XcUserExt getUserExt(String username);
}

package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "认证服务接口")
public interface AuthControllerApi {

    @ApiOperation("用户登录")
    public LoginResult login(LoginRequest loginRequest, HttpServletResponse response);


}

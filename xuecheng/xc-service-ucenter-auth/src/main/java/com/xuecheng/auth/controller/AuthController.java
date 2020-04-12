package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class AuthController implements AuthControllerApi {

    @Autowired
    private AuthService authService;

    @Value("${auth.cookieDomain}")
    private String domain;

    @Value("${auth.cookieMaxAge}")
    private Integer maxAge;

    @Override
    @PostMapping("userlogin")
    public LoginResult login(@Valid LoginRequest loginRequest, HttpServletResponse response) {

        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getVerifycode());
        String access_token = authToken.getAccess_token();
        CookieUtil.addCookie(response,domain,"/","uid",access_token,maxAge,false);

        return new LoginResult(CommonCode.SUCCESS,access_token);
    }

    @GetMapping("userjwt")
    public JwtResult getUserJwt(@CookieValue("uid")String uid){
        AuthToken authToken = this.authService.getAuthToken(uid);
        String jwt_token = authToken.getJwt_token();
        return new JwtResult(CommonCode.SUCCESS,jwt_token);
    }


    @PostMapping("userlogout")
    public ResponseResult logout(@CookieValue("uid")String uid,HttpServletResponse response){
        // 从redis中删除token
        this.authService.delToken(uid);
        // 从cookie中删除token信息
        CookieUtil.addCookie(response,domain,"/","uid",uid,0,false);
        return new ResponseResult(CommonCode.SUCCESS);
    }



}

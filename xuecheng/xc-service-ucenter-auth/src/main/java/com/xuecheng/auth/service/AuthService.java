package com.xuecheng.auth.service;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;

public interface AuthService {

    AuthToken login(String username,String password,String verifyCode);

    AuthToken getAuthToken(String uid);

    void delToken(String uid);
}

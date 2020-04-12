package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    protected static final String KEY_PREFIX = "user:token:";
    protected static final long EXPIRE = 1800;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *  需求:
     *         从cookie中获取token
     *         从header中获取token
     *         从redis中获取token
     */
    public String getTokenFromCoolie(HttpServletRequest request){
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String uid = cookieMap.get("uid");
        if(cookieMap == null || StringUtils.isBlank(uid)){
            return null;
        }
        return uid;
    }


    // 从header中取
    public String getTokenFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isBlank(authorization)){
            return null;
        }
        return authorization;
    }

    // 从redis中判断是否过期
    public boolean getTokenFromRedis(String access_token){
        Long expire = stringRedisTemplate.getExpire(KEY_PREFIX + access_token, TimeUnit.SECONDS);
        //  小于6分钟则延长过期时间
        if(0 < expire && expire < 600){
            stringRedisTemplate.expire(KEY_PREFIX + access_token,EXPIRE,TimeUnit.SECONDS);
        }
        return  expire > 0;
    }




}

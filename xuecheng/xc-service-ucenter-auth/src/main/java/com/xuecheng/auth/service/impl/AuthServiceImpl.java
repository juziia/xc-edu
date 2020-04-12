package com.xuecheng.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String KEY_PREFIX = "user:token:";

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.tokenValiditySeconds}")
    private Integer tokenValiditySeconds;



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public AuthToken login(String username, String password, String verifyCode) {

        // 申请令牌
        AuthToken authToken = this.applyToken(username,password,clientId,clientSecret);


        // 讲authToken存入redis中
        long expire = this.saveToken(authToken,tokenValiditySeconds);
        if(expire <= 0){
            CastException.cast(AuthCode.AUTH_SAVE_TOKEN_ERROR);
        }

        return authToken;
    }

    @Override
    public AuthToken getAuthToken(String uid) {
        String key = KEY_PREFIX + uid;
        String value = stringRedisTemplate.opsForValue().get(key);
        if(value == null){
            return null;
        }

        AuthToken authToken = null;
        try {
            authToken = JSON.parseObject(value, AuthToken.class);
            return authToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }

    @Override
    public void delToken(String uid) {
        stringRedisTemplate.delete(KEY_PREFIX + uid);
    }

    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        // 使用负载均衡器获取认证服务的实例
        ServiceInstance authServiceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if(authServiceInstance == null){
            CastException.cast(AuthCode.AUTH_SERVICE_ERROR);
        }
        // 获取认证服务的IP地址
        URI uri = authServiceInstance.getUri();
        // 定义申请令牌的url
        String tokenUrl = uri + "/auth/oauth/token";

        MultiValueMap<String, String> header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION,this.basicStr(clientId,clientSecret));
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body,header);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // 返回400 或 401都无需处理,正常返回响应信息
                if(response.getRawStatusCode() == 400 && response.getRawStatusCode() == 401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, Map.class);
        Map entityBody = responseEntity.getBody();
        if(entityBody == null){
            // 生曾token令牌错误  error -> unauthorized  error -> invalid_grant
            CastException.cast(CommonCode.SERVER_ERROR);

            return null;
        }
        String accessToken = (String) entityBody.get("access_token");
        String refreshToken = (String) entityBody.get("refresh_token");
        String jti = (String) entityBody.get("jti");
        if(StringUtils.isBlank(accessToken) || StringUtils.isBlank(refreshToken) || StringUtils.isBlank(jti)){
            if(entityBody.get("error_description") != null){
                String error_description = entityBody.get("error_description").toString();
                if(error_description.contains("UserDetailsService returned null")){
                    CastException.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
                error_description = entityBody.get("error_description").toString();
                if(error_description.contains("用户名或密码错误")){
                    CastException.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
            }
            return null;
        }

        // 创建用户对象
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token(jti);
        authToken.setJwt_token(accessToken);
        authToken.setRefresh_token(refreshToken);

        return authToken;
    }

    private String basicStr(String clientId, String clientSecret) {
        String base64Str = clientId + ":" +clientSecret;
        byte[] encode = Base64Utils.encode(base64Str.getBytes());
        return "Basic "+new String(encode);
    }

    private long saveToken(AuthToken authToken, int ttl) {
        String key = KEY_PREFIX + authToken.getAccess_token();
        String json = JSON.toJSONString(authToken);
        stringRedisTemplate.boundValueOps(key).set(json,ttl, TimeUnit.SECONDS);
        return stringRedisTemplate.getExpire(key,TimeUnit.SECONDS);

    }
}

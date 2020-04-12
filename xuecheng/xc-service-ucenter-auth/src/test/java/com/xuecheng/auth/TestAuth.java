package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAuth {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;


    @Test
    public void testToken(){
        // 根据微服务名称向eureka获取认证服务的信息
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        // 获取认证服务实例的信息
        URI uri = serviceInstance.getUri();
        // 认证路径是 http://localhost:40400/auth/oauth/token
        String authUrl = uri + "/auth/oauth/token";
        MultiValueMap<String, String>  header = new HttpHeaders();

        String baseString = this.auth("XcWebApp","XcWebApp");

        header.add(HttpHeaders.AUTHORIZATION,baseString);

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");

        //使用restTemplate发起请求
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        DefaultResponseErrorHandler defaultResponseErrorHandler = new DefaultResponseErrorHandler();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() == 400 && response.getRawStatusCode() == 401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        Map bodyMap = responseEntity.getBody();
        System.out.println(bodyMap);


    }

    private String auth(String clientId, String clientPassword) {
        String encodeStr = clientId+":"+clientPassword;
        byte[] encode = Base64Utils.encode(encodeStr.getBytes());

        return "Basic "+new String(encode);
    }


}

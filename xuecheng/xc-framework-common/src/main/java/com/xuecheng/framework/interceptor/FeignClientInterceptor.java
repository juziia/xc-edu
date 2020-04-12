package com.xuecheng.framework.interceptor;


import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes;

public class FeignClientInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取所有的请求头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        if(headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                // 将请求头信息传递给下游服务
                if("authorization".equalsIgnoreCase(headerName) || "cookie".equalsIgnoreCase(headerName)) {
                    String headerValue = request.getHeader(headerName);
                    requestTemplate.header(headerName, headerValue);
                }
            }
        }
    }
}

package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;



    /**
     *  过滤器类型
     *      pre: 在请求被路由之前执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     *  数字越小,执行优先级就越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     *  是否执行run方法
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     *  filter的核心业务逻辑方法
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();

        String headerToken = authService.getTokenFromHeader(requestContext.getRequest());
        if(StringUtils.isBlank(headerToken)){
            // 拒绝访问
            access_denied(requestContext);
            return null;
        }
        String tokenCookie = authService.getTokenFromCoolie(requestContext.getRequest());
        if(StringUtils.isBlank(tokenCookie)){
            access_denied(requestContext);
            return null;
        }
        boolean result = authService.getTokenFromRedis(tokenCookie);
        if(!result){
            access_denied(requestContext);
            return null;
        }

        return null;

    }

    /**
     *  拒绝访问方法
     */
    private void access_denied(RequestContext requestContext){
        requestContext.setSendZuulResponse(false);      // 拒绝转发请求
        requestContext.addZuulResponseHeader("Content-Type","application/json;charset=utf-8");
        requestContext.setResponseStatusCode(401);
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonStr = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(jsonStr);
    }

}

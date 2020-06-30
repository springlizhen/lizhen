package com.chinags.zuul.filter.base;

import com.alibaba.fastjson.JSON;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.StringUtils;
import com.chinags.zuul.filter.FilterShould;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class BaseDataFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        boolean urlStatus = FilterShould.urlFilter(currentContext);
        return urlStatus;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        boolean noFilter = FilterShould.noFilter(currentContext);
        if(noFilter) {
            return null;
        }
        String header = request.getHeader("Authorization");
        if(StringUtils.isNotEmpty(header)){
            //如果有包含有Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")){
                //得到token
                String token = header.substring(7);
                //对令牌进行验证
                try {
                    JwtUtil.parseJWT(token);
                }catch (Exception e){
                    FilterShould.errorMessage(currentContext, StatusCode.TOKENNULLERROR,"token错误或过期！");
                }
            }
        }else{
            FilterShould.errorMessage(currentContext, StatusCode.TOKENNULLERROR,"请携带token访问！");
        }
        return null;
    }
}

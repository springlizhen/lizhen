package com.chinags.zuul.filter.system;

import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.StringUtils;
import com.chinags.zuul.filter.FilterShould;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SystemDataFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        boolean urlStatus = FilterShould.urlFilter(currentContext);
        return !urlStatus;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        boolean noFilter = FilterShould.noFilter(currentContext);
        if(noFilter) {
            return null;
        }
        String appSecretId  = request.getParameter("appSecretId");
        String token = request.getParameter("token");
        if(StringUtils.isNotEmpty(token)&&StringUtils.isNotEmpty(appSecretId)){
            //对令牌进行验证
            try {
                JwtUtil.parseJWTSystem(token, appSecretId);
            }catch (Exception e){
                FilterShould.errorMessage(currentContext, StatusCode.ERROR,"令牌失效或不存在！");
            }
        }else{
            FilterShould.errorMessage(currentContext, StatusCode.ERROR,"请携带token和系统名称编码访问！");
        }
        return null;
    }
}

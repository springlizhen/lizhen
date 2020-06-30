package com.chinags.zuul.filter.system;

import com.chinags.common.entity.StatusCode;
import com.chinags.zuul.filter.FilterShould;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class SystemErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext currentContext = RequestContext.getCurrentContext();
            FilterShould.errorMessage(currentContext, StatusCode.ERROR,"获取数据失败,请联系管理员！");
        } catch (Exception var5) {
            ReflectionUtils.rethrowRuntimeException(var5);
        }
        return null;
    }
}

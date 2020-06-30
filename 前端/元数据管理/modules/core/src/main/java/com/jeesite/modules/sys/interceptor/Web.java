/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.interceptor;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 * @author ThinkGem
 * @version 2018-08-11
 */
public class Web extends BaseService implements HandlerInterceptor {

	protected String adminPath;

	public static String authPath = OALoginUtiles.ippostPrefix + OALoginUtiles.addressPath+"?systemname="+OALoginUtiles.authName;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
		Object o = request.getSession().getAttribute(cookie);
		// 如果已经登录，则跳转到管理首页
		String url = request.getRequestURI();
		adminPath = Global.getProperty("adminPath");
		String path = Global.getProperty("server.servlet.context-path");
//		if(url.indexOf("login")!=-1){
//			if(cookie!=null&&o!=null)
//				ServletUtils.redirectUrl(request, response, adminPath + "/index");
//			else
//				return true;
//		}
        if(url.equals(path+adminPath+"/index")){
            Integer code = token(request);
            if(code!=null&&code==20006){
                response.sendRedirect(authPath);
                return false;
            }
            return true;
        }
		if(url.indexOf("static")!=-1||url.indexOf(".js")!=-1||url.indexOf("error")!=-1){
			return true;
		}
		if(cookie==null||o==null){
			response.sendRedirect(authPath);
			return false;
		}
        Integer code = token(request);
        if(code!=null&&code==20006){
            response.sendRedirect(authPath);
            return false;
        }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private Integer token(HttpServletRequest request){
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            Result result = OALoginUtiles.authGet(request, null, "/sso/token");
            Integer code = result.getCode();
            return code;
        }
        return null;
    }
}

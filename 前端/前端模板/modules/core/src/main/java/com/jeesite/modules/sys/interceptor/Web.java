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
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.JwtUtil;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 *
 * @author ThinkGem
 * @version 2018-08-11
 */
public class Web extends BaseService implements HandlerInterceptor {

    protected String adminPath;

    public static String authPath = OALoginUtiles.ippostPrefix + OALoginUtiles.addressPath + "?systemname=" + OALoginUtiles.authName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 如果已经登录，则跳转到管理首页
        String url = request.getRequestURI();
        if (url.indexOf("static") != -1 || url.indexOf(".js") != -1 || url.indexOf("error") != -1) {
            return true;
        }

        adminPath = Global.getProperty("adminPath");
        String path = Global.getProperty("server.servlet.context-path");
        if (url.equals(path + adminPath + "/login")) {
            response.sendRedirect(path + adminPath + "/index");
            return false;
        }
        if (url.equals(path + "/")) {
            response.sendRedirect(path + adminPath + "/portal/index");
            return false;
        }
        if (url.equals(path + adminPath + "/portal/index")) {
            return true;
        }
        if (url.equals(path + adminPath + "/index") || url.equals(path + adminPath + "/reload")) {
            return true;
        }
        Integer code = token(request);
        if (code != null && code == 20006) {
            response.sendRedirect(path + adminPath + "/reload");
            return false;
        }
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        Object o = request.getSession().getAttribute(cookie);
        if (o == null && cookie != null) {
            Claims claims;
            try {
                claims = JwtUtil.parseJWT(cookie);
            } catch (Exception e) {
                response.sendRedirect(authPath);
                return false;
            }
            User user = new User();
            user.setUserName((String) claims.get("sub"));
            request.getSession().setAttribute(cookie, user);
            o = user;
        }
        if (cookie == null || o == null) {
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

    private Integer token(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            Result result = OALoginUtiles.authGet(request, null, "/sso/token");
            Integer code = result.getCode();
            return code;
        }
        return null;
    }
}

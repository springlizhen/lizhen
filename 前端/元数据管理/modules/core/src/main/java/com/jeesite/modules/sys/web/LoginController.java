/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.interceptor.Web;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.filter.*;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.User;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class LoginController extends BaseController{

	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
		if(token!=null) {
			CookieUtils.setCookie(response, OALoginUtiles.cookieName, token);
			User user = new User();
			user.setUserName(userName);
			request.getSession().setAttribute(token, user);
			ServletUtils.redirectUrl(request,response,adminPath + "/index");
			return null;
		}
		//获取登录用户信息
//		LoginInfo loginInfo = UserUtils.getLoginInfo();
		String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
		Object o = request.getSession().getAttribute(cookie);
		if(cookie==null||o==null){
			try {
				response.sendRedirect(Web.authPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		// 当前用户对象信息
		User user = (User)request.getSession().getAttribute(cookie);

		String params = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PARAMS_PARAM);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, user.getUserName());
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_USERCODE_PARAM, rememberUserCode);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, user.getUserName());
		model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, params);
		model.addAttribute("user", user); // 设置当前用户信息
		Result count = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/count");
		Result dataSum = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/dataSum");
		Result downSum = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/downSum");
		model.addAttribute("count", count.getData());
		model.addAttribute("dataSum", dataSum.getData());
		model.addAttribute("downSum", downSum.getData());
		// 返回主页面视图
		return "modules/sys/index";
	}

	/**
	 * 查询所有主题分类
	 */
	@RequestMapping(value = "ztfl")
	@ResponseBody
	public String ztfl(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
		Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/theme");
		return result.getData();
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "logout")
	public String logout(HttpServletRequest request,  HttpServletResponse response) {
		String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
		request.getSession().removeAttribute(cookie);
		try {
			response.sendRedirect(Web.authPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

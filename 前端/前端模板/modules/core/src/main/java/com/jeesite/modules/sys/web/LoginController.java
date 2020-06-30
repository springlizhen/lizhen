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
import com.jeesite.modules.sys.utils.JwtUtil;
import com.jeesite.modules.sys.utils.OAConfigUtil;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@RequestMapping(value = "reload")
	public String reload(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
		return "error/reload";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
		String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
		Claims claims;
		try {
			if(token!=null) {
				claims = JwtUtil.parseJWT(token);
				CookieUtils.setCookie(response, OALoginUtiles.cookieName, token);
				User user = new User();
				user.setUserName((String) claims.get("sub"));
				user.setLoginCode((String) claims.get("loginCode"));
				request.getSession().setAttribute(token, user);
				ServletUtils.redirectUrl(request,response,adminPath + "/index");
				return null;
			}else{
					claims = JwtUtil.parseJWT(cookie);
			}
		}catch (Exception e){
			e.printStackTrace();
			try {
				response.sendRedirect(Web.authPath);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		//获取登录用户信息
		Object o = request.getSession().getAttribute(cookie);
		if(cookie==null){
			try {
				response.sendRedirect(Web.authPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}else if(o==null){
			ServletUtils.redirectUrl(request,response,adminPath + "/index?token="+cookie);
			return null;
		}
		// 当前用户对象信息
		User user = (User)request.getSession().getAttribute(cookie);

		String params = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PARAMS_PARAM);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, user.getUserName());
		model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, user.getUserName());
		model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, params);
		model.addAttribute("user", user); // 设置当前用户信息
		model.addAttribute("OA", OAConfigUtil.getByName("oaHZhttp")+"workflow/manager/index.html?loginName="+claims.get("loginCode")+"&password="+claims.get("pm")+"&_version="+System.currentTimeMillis());
		// 返回主页面视图
		return "modules/sys/sysIndex";
	}

	/**
	 * 获取当前用户菜单数据
	 */
	@RequestMapping(value = "menuTree")
	@ResponseBody
	public String menuTree(HttpServletRequest request, HttpServletResponse response) {
		Result data = OALoginUtiles.authGet(request,null,"/menu/leftList");
		return data.getData();
	}

	/**
	 * 切换系统菜单（仅超级管理员有权限）
	 */
	@RequestMapping(value = "switch/{sysCode}")
	public String switchSys(@PathVariable String sysCode) {
		User user = UserUtils.getUser();
		if (user.isSuperAdmin() && StringUtils.isNotBlank(sysCode)){
			Session session = UserUtils.getSession();
			session.setAttribute("sysCode", sysCode);
			UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		}
		return REDIRECT + adminPath + "/index";
	}

	/**
	 * 切换主题
	 */
	@RequestMapping(value = "switchSkin/{skinName}")
	public String switchSkin(@PathVariable String skinName, HttpServletRequest request, HttpServletResponse response) {
		LoginInfo loginInfo = UserUtils.getLoginInfo();
		if (StringUtils.isNotBlank(skinName) && !"select".equals(skinName)){
			CookieUtils.setCookie(response, "skinName_" + loginInfo.getId(), skinName);
			return REDIRECT + adminPath + "/index";
		}
		return "modules/sys/sysSwitchSkin";
	}

//	/**
////	 * 工程页面
////	 */
////	@RequestMapping(value = "desktop")
////	public String desktop(HttpServletRequest request, HttpServletResponse response, Model model) {
////		return "modules/sys/sysDesktop";
////	}
	/**
	 * 合同首页页面
	 */
	@RequestMapping(value = "desktop")
	public String desktop(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/sysDesktopTo";
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

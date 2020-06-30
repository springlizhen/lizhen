/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.PostService;

/**
 * 岗位管理Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/post")
public class PostController extends BaseController {

	@Autowired
	private PostService postService;
	
	@ModelAttribute
	public Map get(String postCode, HttpServletRequest request) {
		if(postCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("postCode", postCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/post/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	@RequestMapping(value = "list")
	public String list(Post post, Model model) {
		return "modules/sys/postList";
	}

	@RequestMapping(value = {"listData"})
	@ResponseBody
	public String listData(Post post, HttpServletRequest request, HttpServletResponse response) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/pageList");
		return data.getData();
	}

	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		JSONObject post = (JSONObject) map.get("map");
		model.addAttribute("post", post);
		return "modules/sys/postForm";
	}

	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/post/saveAndUpdate");
		if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Post post, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/disable");
		return renderResult(Global.TRUE, data.getMessage());
	}

	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Post post, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/enable");
		return renderResult(Global.TRUE, data.getMessage());
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/delete");
		if(data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}
	
	/**
	 * 验证岗位名是否有效
	 * @param oldPostName
	 * @param postName
	 * @return
	 */
	@RequestMapping(value = "checkPostName")
	@ResponseBody
	public String checkPostName(String oldPostName, String postName, HttpServletRequest request) {
		Map map = new HashMap();
		if (postName != null && postName.equals(oldPostName)) {
			return Global.TRUE;
		} else if (postName != null && OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/checkPostName").getMessage().equals("true")) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	/**
	 * 获取岗位树结构数据
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/treeData");
		return data.getData();
	}
	
}
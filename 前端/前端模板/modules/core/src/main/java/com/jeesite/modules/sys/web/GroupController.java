/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.OAUserUtils;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 岗位管理Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/group")
public class GroupController extends BaseController {

	@Autowired
	private PostService postService;
	
	@ModelAttribute
	public Map get(String id, HttpServletRequest request) {
		if(id!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
			Result data = OALoginUtiles.authGet(request,dataMap,"/group/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	@RequestMapping(value = "list")
	public String list(Post post, Model model) {
		return "modules/sys/group/groupList";
	}

	@RequestMapping(value = {"listData"})
	@ResponseBody
	public String listData(Post post, HttpServletRequest request, HttpServletResponse response) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/group/pageList");
		return data.getData();
	}

	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		JSONObject group = (JSONObject) map.get("map");
		model.addAttribute("group", group);
		return "modules/sys/group/groupForm";
	}

	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/group/saveAndUpdate");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/group/delete");
		if(data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	@RequestMapping(value = "userTreeData")
	@ResponseBody
	public String userTreeData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/empUser/treeData");
		if(data.getCode()==20000) {
			return data.getData();
		}
		else {
			return data.getData();
		}
	}
	
}
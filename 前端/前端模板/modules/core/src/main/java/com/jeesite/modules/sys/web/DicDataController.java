/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.web.user.EmpUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 机构Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dicData")
public class DicDataController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@Autowired
	private EmpUserController empUserController;

	/**
	 * 获取机构
	 */
	@ModelAttribute
	public Map get(String dictCode, String parentCode, HttpServletRequest request) {
		if(StringUtils.isNotEmpty(dictCode) || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("dictCode", dictCode);
			dataMap.put("parentCode", parentCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/dicData/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 机构列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, String dictType, String dictName, HttpServletRequest request) {
		String sys_status = DictionaryUtils.setDicData("sys_status", request);
		String sys_yes_no = DictionaryUtils.setDicData("sys_yes_no", request);
		model.addAttribute("sys_status",sys_status);
		model.addAttribute("sys_yes_no",sys_yes_no);
		model.addAttribute("dictType", dictType);
		model.addAttribute("dictName", dictName);
		return "modules/sys/dictDataList";
	}

	/**
	 * 机构列表
	 */
	@RequestMapping(value = "listbj")
	public String listbj(Model model, String id, HttpServletRequest request) {
		Map dataMap = MapUtils.newHashMap();
		dataMap.put("id", id);
		Result data = OALoginUtiles.authGet(request,dataMap,"/dicType/form");
		String sys_status = DictionaryUtils.setDicData("sys_status", request);
		String sys_yes_no = DictionaryUtils.setDicData("sys_yes_no", request);
		model.addAttribute("sys_status",sys_status);
		model.addAttribute("sys_yes_no",sys_yes_no);
		model.addAttribute("dictType", JSONObject.parseObject(data.getData()).get("dictType"));
		model.addAttribute("dictName", JSONObject.parseObject(data.getData()).get("dictName"));
		return "modules/sys/dictDataList";
	}

	/**
	 * 查询机构数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicData/pageList");
		return data.getData();
	}

	/**
	 * 查看编辑机构
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map, String dictType) {
		// 创建并初始化下一个节点信息
		JSONObject dicData = (JSONObject) map.get("map");
		model.addAttribute("dicData",dicData);
		model.addAttribute("dictType",dictType);
		return "modules/sys/dictDataForm";
	}

	/**
	 * 保存机构
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/dicData/saveAndUpdate");
		if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 停用机构
	 */
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicData/disable");
		if(data.getCode()==20000){
			return renderResult(Global.TRUE, data.getMessage());
		}else{
			return renderResult(Global.FALSE, data.getMessage());
		}
	}

	/**
	 * 启用机构
	 */
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicData/enable");
		return renderResult(Global.TRUE, data.getMessage());
	}

	/**
	 * 删除机构
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicData/delete");
		if(data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}
}

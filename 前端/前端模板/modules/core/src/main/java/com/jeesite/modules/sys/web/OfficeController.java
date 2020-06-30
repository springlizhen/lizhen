/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.*;
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
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@Autowired
	private EmpUserController empUserController;

	/**
	 * 获取机构
	 */
	@ModelAttribute
	public Map get(String officeCode, String parentCode, HttpServletRequest request) {
		if(officeCode!=null || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("officeCode", officeCode);
			dataMap.put("parentCode", parentCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/office/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 机构列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("status", DictionaryUtils.setMap(null,2L,"正常","停用"));
		String sys_status = DictionaryUtils.setDicData("sys_status", request);
		model.addAttribute("sys_status",sys_status);
		String office_level = DictionaryUtils.setDicData("office_level", request);
		model.addAttribute("office_level",office_level);
		String sys_office_type = DictionaryUtils.setDicData("sys_office_type", request);
		model.addAttribute("sys_office_type",sys_office_type);
		return "modules/sys/officeList";
	}
	
	/**
	 * 查询机构数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/pageList");
		return data.getData();
	}

	/**
	 * 查看编辑机构
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject office = (JSONObject) map.get("map");
		model.addAttribute("office",office);
		List office_istrative = DictionaryUtils.setDicDataListSelect("office_istrative", request);
		model.addAttribute("office_istrative", office_istrative);
		List office_level = DictionaryUtils.setDicDataListSelect("office_level", request);
		model.addAttribute("office_level", office_level);
		List local_classification = DictionaryUtils.setDicDataListSelect("local_classification", request);
		model.addAttribute("local_classification", local_classification);
		List office_unit = DictionaryUtils.setDicDataListSelect("office_unit", request);
		model.addAttribute("office_unit", office_unit);
		List sys_office_jghf = DictionaryUtils.setDicDataListSelect("sys_office_jghf", request);
		model.addAttribute("sys_office_jghf", sys_office_jghf);
		return "modules/sys/officeForm";
	}

	/**
	 * 保存机构
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/office/saveAndUpdate");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
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
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/disable");
		if(data.getCode()==20000){
			OAUserUtils.oaSyn();
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
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/enable");
		OAUserUtils.oaSyn();
		return renderResult(Global.TRUE, data.getMessage());
	}

	/**
	 * 删除机构
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/delete");
		if(data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 获取机构树结构数据
	 * @param excludeCode		排除的ID
	 * @param parentCode	上级Code
	 * @param isAll			是否显示所有机构（true：不进行权限过滤）
	 * @param officeTypes	机构类型（1：省级公司；2：市级公司；3：部门）
	 * @param companyCode	仅查询公司下的机构
	 * @param isShowCode	是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @param isShowFullName 是否显示全机构名称
	 * @param isLoadUser	是否加载机构下的用户
	 * @param postCode		机构下的用户过滤岗位
	 * @param roleCode		机构下的用户过滤角色
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(String excludeCode, String parentCode, Boolean isAll,
			String officeTypes, String companyCode, String isShowCode, String isShowFullName,
			Boolean isLoadUser, String postCode, String roleCode, String ctrlPermi, HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/treeData");
		return data.getData();
	}

	@RequestMapping(value = "treeDataPeople")
	@ResponseBody
	public String treeDataPeople(String excludeCode, String parentCode, Boolean isAll,
						   String officeTypes, String companyCode, String isShowCode, String isShowFullName,
						   Boolean isLoadUser, String postCode, String roleCode, String ctrlPermi, HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/office/treeDataPeople");
		return data.getData();
	}

	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData() {
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		officeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}

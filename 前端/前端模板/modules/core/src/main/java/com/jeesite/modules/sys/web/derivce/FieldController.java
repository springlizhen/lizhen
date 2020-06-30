/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.derivce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
 * 代码字段Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/field")
public class FieldController extends BaseController {

	/**
	 * 获取代码字段
	 */
	@ModelAttribute
	public Map get(String id,HttpServletRequest request) {
		if(id!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/field/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 代码字段列表
	 */
	@RequestMapping(value = "index")
	public String index(Model model) {
		return "modules/sys/derivce/field/fieldIndex";
	}

	/**
	 * 代码字段列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		List sys_field_type = DictionaryUtils.setDicDataListSelect("sys_field_type", request);
		model.addAttribute("sys_field_type", sys_field_type);
		List sys_field_fieldClass = DictionaryUtils.setDicDataListSelect("sys_field_fieldClass", request);
		model.addAttribute("sys_field_fieldClass", sys_field_fieldClass);
		String sys_field_fieldRequired = DictionaryUtils.setDicData("sys_field_fieldRequired", request);
		model.addAttribute("sys_field_fieldRequired", sys_field_fieldRequired);
		String sysFieldFieldClass = DictionaryUtils.setDicData("sys_field_fieldClass", request);
		model.addAttribute("sysFieldFieldClass", sysFieldFieldClass);
		String sysFieldType = DictionaryUtils.setDicData("sys_field_type", request);
		model.addAttribute("sysFieldType", sysFieldType);
		return "modules/sys/derivce/field/fieldList";
	}

	/**
	 * 代码字段列表
	 */
	@RequestMapping(value = "listpb")
	public String listpb(Model model, HttpServletRequest request) {
		List sys_field_type = DictionaryUtils.setDicDataListSelect("sys_field_type", request);
		model.addAttribute("sys_field_type", sys_field_type);
		List sys_field_fieldClass = DictionaryUtils.setDicDataListSelect("sys_field_fieldClass", request);
		model.addAttribute("sys_field_fieldClass", sys_field_fieldClass);
		String sys_field_fieldRequired = DictionaryUtils.setDicData("sys_field_fieldRequired", request);
		model.addAttribute("sys_field_fieldRequired", sys_field_fieldRequired);
		String sysFieldFieldClass = DictionaryUtils.setDicData("sys_field_fieldClass", request);
		model.addAttribute("sysFieldFieldClass", sysFieldFieldClass);
		String sysFieldType = DictionaryUtils.setDicData("sys_field_type", request);
		model.addAttribute("sysFieldType", sysFieldType);
		return "modules/sys/derivce/fieldList";
	}
	
	/**
	 * 查询代码字段数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/field/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	/**
	 * 查询代码字段数据
	 */
	@RequestMapping(value = "findAll")
	@ResponseBody
	public String findAll(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/field/findAll");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 查看代码字段
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map, String fieldGroup, String fieldGroupName) {
		model.addAttribute("fieldGroup",fieldGroup);
		model.addAttribute("fieldGroupName",fieldGroupName);
		// 创建并初始化下一个节点信息
		JSONObject field = (JSONObject) map.get("map");
		model.addAttribute("field",field);
		List sys_field_type = DictionaryUtils.setDicDataListSelect("sys_field_type", request);
		model.addAttribute("sys_field_type", sys_field_type);
		List sys_field_fieldClass = DictionaryUtils.setDicDataListSelect("sys_field_fieldClass", request);
		model.addAttribute("sys_field_fieldClass", sys_field_fieldClass);
		List sys_field_fieldRequired = DictionaryUtils.setDicDataListSelect("sys_field_fieldRequired", request);
		model.addAttribute("sys_field_fieldRequired", sys_field_fieldRequired);
		return "modules/sys/derivce/field/fieldForm";
	}

	/**
	 * 查看代码字段
	 */
	@RequestMapping(value = "formpb")
	public String formpb(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject field = (JSONObject) map.get("map");
		model.addAttribute("field",field);
		List sys_field_type = DictionaryUtils.setDicDataListSelect("sys_field_type", request);
		model.addAttribute("sys_field_type", sys_field_type);
		List sys_field_fieldClass = DictionaryUtils.setDicDataListSelect("sys_field_fieldClass", request);
		model.addAttribute("sys_field_fieldClass", sys_field_fieldClass);
		List sys_field_fieldRequired = DictionaryUtils.setDicDataListSelect("sys_field_fieldRequired", request);
		model.addAttribute("sys_field_fieldRequired", sys_field_fieldRequired);
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/derviceOfficePb/findAll");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		Object o = data.getData();
		model.addAttribute("fieldGroup", JSONArray.parseArray((String) o));
		return "modules/sys/derivce/fieldForm";
	}

	/**
	 * 保存代码字段
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/field/saveAndUpdate");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 删除代码字段
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/field/delete");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if(data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/field/treeData");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	
}

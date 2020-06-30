/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.derivce;

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
import com.jeesite.modules.sys.web.user.EmpUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 类型Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sensorType")
public class SensorTypeController extends BaseController {

	/**
	 * 获取类型
	 */
	@ModelAttribute
	public Map get(String id, String parentCode, HttpServletRequest request) {
		if(StringUtils.isNotEmpty(id) || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
			dataMap.put("parentCode", parentCode);
			Result data = OALoginUtiles.dataGet(request,dataMap,"/sensorType/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 类型列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {

		return "modules/sys/derivce/sensorDataList";
	}
	
	/**
	 * 查询类型数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/sensorType/pageList");
		return data.getData();
	}

	/**
	 * 查看编辑类型
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map, String dictType) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("sensor",sensor);
		return "modules/sys/derivce/sensorDataForm";
	}

	/**
	 * 保存类型
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request),"/sensorType/saveAndUpdate");
		if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 删除类型
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/sensorType/delete");
		if(data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 获取机构树结构数据
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData( HttpServletRequest request) {
		Result data = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/sensorType/treeData");
		return data.getData();
	}
}

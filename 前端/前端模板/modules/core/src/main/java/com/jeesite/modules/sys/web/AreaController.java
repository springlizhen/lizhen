/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;

import javax.servlet.http.HttpServletRequest;

/**
 * 区域Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	/**
	 * 获取区域
	 */
	@ModelAttribute
	public Map get(String areaCode, String parentCode, HttpServletRequest request) {
		if(areaCode!=null || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("areaCode", areaCode);
			dataMap.put("parentCode", parentCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/area/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}
	
	/**
	 * 区域列表
	 * @param model
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("status", DictionaryUtils.setMap(null,2L,"正常","停用"));
		return "modules/sys/areaList";
	}
	
	/**
	 * 查询区域数据
	 * @param request
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/area/pageList");
		return data.getData();
	}
	
	/**
	 * 查看编辑区域
	 * @param map
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject area = (JSONObject) map.get("map");
		model.addAttribute("area",area);
		return "modules/sys/areaForm";
	}
	
	/**
	 * 保存区域
	 * @param request
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/area/saveAndUpdate");
		if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 停用区域
	 */
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/area/disable");
		if(data.getCode()==20000){
			return renderResult(Global.TRUE, data.getMessage());
		}else{
			return renderResult(Global.FALSE, data.getMessage());
		}
	}

	/**
	 * 启用区域
	 */
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/area/enable");
		return renderResult(Global.TRUE, data.getMessage());
	}

	/**
	 * 删除区域
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/area/delete");
        if(data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
        else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 获取区域树结构数据
	 * @param excludeCode 排除的Code\
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(String excludeCode, HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/area/treeData");
		return data.getData();
	}

	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		areaService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}

//	/**
//	 * 创建并初始化下一个节点信息，如：排序号、默认值
//	 */
//	@RequestMapping(value = "createNextNode")
//	@ResponseBody
//	public Area createNextNode(Area area) {
//		if (StringUtils.isNotBlank(area.getParentCode())) {
//			area.setParent(areaService.get(area.getParentCode()));
//		}
//		if (area.getIsNewRecord()) {
//			Area where = new Area();
//			where.setParentCode(area.getParentCode());
//			Area last = areaService.getLastByParentCode(where);
//			// 获取到下级最后一个节点
//			if (last != null){
//				area.setTreeSort(last.getTreeSort() + 30);
//				area.setAreaCode(IdGen.nextCode(last.getAreaCode()));
//			}else if(area.getParent() != null){
//				area.setAreaCode(area.getParent() + "001");
//			}
//		}
//		// 以下设置表单默认数据
//		if (area.getTreeSort() == null){
//			area.setTreeSort(Area.DEFAULT_TREE_SORT);
//		}
//		return area;
//	}
}

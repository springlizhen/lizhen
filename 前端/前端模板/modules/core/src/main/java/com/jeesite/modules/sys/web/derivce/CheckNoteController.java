/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.derivce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/checkNote")
public class CheckNoteController extends BaseController {

	/**
	 * 获取分类
	 */
	@ModelAttribute
	public Map get(String id,  HttpServletRequest request) {
		Map dataMap = MapUtils.newHashMap();
		Result data = null;
		if(id!=null ) {
			dataMap.put("id", id);
		}
		data = OALoginUtiles.dataGet(request,dataMap,"/checkNote/selectCheck");
		return (Map) JSON.parse(data.getData());
	}
	@RequestMapping(value = "listCheckSelect")
	public String listCheckSelect(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/checkSelect";
	}

	@RequestMapping(value = "listCheck")
	public String listCheck(Model model, HttpServletRequest request,Map map) {
		Map<String,Object> map1=RequestParameter.requestParamMap(request);
		JSONObject check = (JSONObject) map.get("map");
		model.addAttribute("check",map1);
		return "modules/sys/derivce/checknote/check";
	}
	/**
	 * 分类列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		List<Map<String,Object>> list=null;
		Result data=null;
		Map dataMap = MapUtils.newHashMap();
		Map manageStation = MapUtils.newHashMap();
		Map manageOffice = MapUtils.newHashMap();
		manageOffice.put("officeLevel","管理所");
		manageStation.put("officeLevel","管理站");
		dataMap.put("officeLevel","分中心");
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp",dataMap	,"/checkNote/selectOffice");
			list=(List)JSON.parse(data.getData());
			model.addAttribute("checkCenter",list);
			data = OALoginUtiles.sysGet(request, "systemhttp",manageStation,"/checkNote/selectOffice");
			list=(List)JSON.parse(data.getData());
			model.addAttribute("manageStation",list);
			data = OALoginUtiles.sysGet(request, "systemhttp",manageOffice,"/checkNote/selectOffice");
			list=(List)JSON.parse(data.getData());
			model.addAttribute("manageOffice",list);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return "modules/sys/derivce/checknote/checkList";
	}

	/**
	 * 查询分类数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = null;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Map map=RequestParameter.requestParamMap(request);
			Object data1 = map.get("partoal");
			if(null !=data1){
				map.put("kb",data1.toString());
				map.remove("partoal");
			}
			data = OALoginUtiles.cdPostTo(request,map,"/checkNote/select");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	/**
	 * 查询分类数据
	 */
	@RequestMapping(value = "listCheckTo")
	@ResponseBody
	public String listCheckTo(HttpServletRequest request) {
		Result data = null;
		try {
			Map map=RequestParameter.requestParamMap(request);
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/checkNote/listCheckTo");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	@RequestMapping(value = "view")
	public String view(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject standard = (JSONObject) map.get("map");
		model.addAttribute("check",standard);
		return "modules/sys/derivce/checknote/checkView";
	}

	/**
	 * 查看编辑分类
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		Result data = null;
		JSONObject standard = (JSONObject) map.get("map");
		model.addAttribute("check",standard);
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/checkNote/selectCheckName");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		List<Map<String,Object>> list=(List)JSON.parse(data.getData());
		model.addAttribute("checkName",list);


		return "modules/sys/derivce/checknote/checkForm";
	}
	/**
	 * 保存分类
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/checkNote/save");
			FileUploadUtils.saveFileUpload((String) RequestParameter.requestParamMap(request).get("itemUpload"), "itemUpload");
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
	 * 删除分类
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/checkNote/delete");
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

}

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
import com.jeesite.modules.sys.entity.DictData;
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
 * 分类Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/derviceOffice")
public class DerivceOfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	/**
	 * 获取分类
	 */
	@ModelAttribute
	public Map get(String officeCode, String parentCode, HttpServletRequest request) {
		if(officeCode!=null || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("officeCode", officeCode);
			dataMap.put("parentCode", parentCode);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/deriviceOffice/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 分类列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("status", DictionaryUtils.setMap(null,2L,"正常","停用"));
		List<DictData> sys_search_status = DictionaryUtils.setDicDataListSelect("sys_search_status", request);
		model.addAttribute("sys_search_status",sys_search_status);
		return "modules/sys/derivce/officeList";
	}
	
	/**
	 * 查询分类数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	/**
	 * 查看编辑分类
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject office = (JSONObject) map.get("map");
		model.addAttribute("office",office);

		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/derviceOfficePb/findAll");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		Object o = data.getData();
		model.addAttribute("fieldGroup", JSONArray.parseArray((String) o));
		return "modules/sys/derivce/officeForm";
	}

	/**
	 * 保存分类
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/saveAndUpdate");
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
	 * 停用分类
	 */
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/disable");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if(data.getCode()==20000){
			return renderResult(Global.TRUE, data.getMessage());
		}else{
			return renderResult(Global.FALSE, data.getMessage());
		}
	}

	/**
	 * 启用分类
	 */
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/enable");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return renderResult(Global.TRUE, data.getMessage());
	}

	/**
	 * 删除分类
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/delete");
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

	/**
	 * 获取分类树结构数据
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/deriviceOffice/treeData");
        } catch (SystemException e) {
            e.printStackTrace();
        }
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

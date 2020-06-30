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
import com.jeesite.modules.sys.utils.DictionaryUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共分类Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/derviceOfficePb")
public class DerviceOfficePbController extends BaseController {

	/**
	 * 公共分类获取
	 */
	@ModelAttribute
	public Map get(String id,HttpServletRequest request) {
		if(id!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/derviceOfficePb/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 公共分类
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {

		return "modules/sys/derivce/officePbList";
	}
	
	/**
	 * 查询公共分类
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/derviceOfficePb/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	/**
	 * 查看公共分类
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject derviceOfficePb = (JSONObject) map.get("map");
		model.addAttribute("derviceOfficePb",derviceOfficePb);
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request,"systemhttp", null,"/field/findAll");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		model.addAttribute("bs", data.getData());
		Map maps = new HashMap();
		maps.put("fieldPb", "1");
		try {
			data = OALoginUtiles.sysGet(request,"systemhttp", maps,"/field/findAll");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		model.addAttribute("bsp", data.getData());
		return "modules/sys/derivce/officePbForm";
	}

	/**
	 * 保存公共分类
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/derviceOfficePb/saveAndUpdate");
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
	 * 删除公共分类
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/derviceOfficePb/delete");
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

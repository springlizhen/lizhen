/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.derivce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.*;
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
 * 设备Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sensor")
public class SensorController extends BaseController {

	@Autowired
	private OfficeService officeService;

	/**
	 * 获取设备
	 */
	@ModelAttribute
	public Map get(String id, HttpServletRequest request) {
		if(id!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/sensor/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 设备列表
	 */
	@RequestMapping(value = "list")
	public String list(String deviceCode, String deviceName, Model model, HttpServletRequest request) {
		model.addAttribute("deviceName", deviceName);
		model.addAttribute("deviceCode", deviceCode);
		return "modules/sys/derivce/sensor/sensorList";
	}
	
	/**
	 * 查询设备数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/sensor/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	/**
	 * 查看编辑设备
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, String deviceCode, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("sensor",sensor);
		model.addAttribute("deviceCode", deviceCode);
		List sys_sensor_signal = DictionaryUtils.setDicDataListSelect("sys_sensor_signal", request);
		model.addAttribute("sys_sensor_signal", sys_sensor_signal);
		return "modules/sys/derivce/sensor/sensorForm";
	}

	/**
	 * 保存设备
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/sensor/saveAndUpdate");
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
	 * 删除设备
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/sensor/delete");
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

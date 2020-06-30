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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 设备Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/device")
public class DeviceController extends BaseController {

	@Autowired
	private OfficeService officeService;

	/**
	 * 获取设备
	 */
	@ModelAttribute
	public Map get(String id, String parentCode, HttpServletRequest request) {
		if(id!=null || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
			if(!"0".equals(parentCode)){
				dataMap.put("parentCode", parentCode);
			}
			Result data = null;
			try {
				data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/device/form");
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
	@RequestMapping(value = "index")
	public String index(Model model) {
		model.addAttribute("op", "device");
		return "modules/sys/derivce/device/deviceIndex";
	}

	/**
	 * 设备列表
	 */
	@RequestMapping(value = "indexOffice")
	public String indexOffice(Model model) {
		model.addAttribute("op", "office");
		return "modules/sys/derivce/device/deviceIndexOffice";
	}

	/**
	 * 设备列表
	 */
	@RequestMapping(value = "list")
	public String list(String op, Model model, HttpServletRequest request) {
		model.addAttribute("op", op);
		List sys_device_ctl = DictionaryUtils.setDicDataListSelect("sys_device_ctl", request);
		model.addAttribute("sys_device_ctl", sys_device_ctl);
		List sys_device_use = DictionaryUtils.setDicDataListSelect("sys_device_use", request);
		model.addAttribute("sys_device_use", sys_device_use);
		String sysDeviceCtl = DictionaryUtils.setDicData("sys_device_ctl", request);
		model.addAttribute("sysDeviceCtl", sysDeviceCtl);
		String sysDeviceUse = DictionaryUtils.setDicData("sys_device_use", request);
		model.addAttribute("sysDeviceUse", sysDeviceUse);
		String officeUnit = DictionaryUtils.setDicData("office_unit", request);
		model.addAttribute("officeUnit", officeUnit);
		return "modules/sys/derivce/device/deviceList";
	}

	/**
	 * 查询设备数据
	 */
	@RequestMapping(value = "deviceMessage")
	@ResponseBody
	public String deviceMessage(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/device/form");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	/**
	 * 根据名称查询所属机构
	 */
	@RequestMapping(value = "kb")
	@ResponseBody
	public String kb(HttpServletRequest request,@RequestParam("id") String id) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp",RequestParameter.requestParamMap(request),"/device/kb");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 查询设备数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/pageList");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 查看编辑设备
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map,String deviceClass, String deviceClassName,String orgId, String orgIdName) {
		model.addAttribute("deviceClass",deviceClass);
		model.addAttribute("deviceClassName",deviceClassName);
		model.addAttribute("orgId",orgId);
		model.addAttribute("orgIdName",orgIdName);
		// 创建并初始化下一个节点信息
		JSONObject device = (JSONObject) map.get("map");
		model.addAttribute("device",device);
		List sys_device_ctl = DictionaryUtils.setDicDataListSelect("sys_device_ctl", request);
		model.addAttribute("sys_device_ctl", sys_device_ctl);
		List sys_device_use = DictionaryUtils.setDicDataListSelect("sys_device_use", request);
		model.addAttribute("sys_device_use", sys_device_use);
		List office_unit = DictionaryUtils.setDicDataListSelect("office_unit", request);
		model.addAttribute("office_unit", office_unit);
		//代码参数类型
		List field = DictionaryUtils.setDicDataList("sys_field_fieldClass", request);
		model.addAttribute("field", field);

		List device_type = DictionaryUtils.setDicDataListSelect("device_type", request);
		model.addAttribute("device_type", device_type);

		List device_cgq = DictionaryUtils.setDicDataListSelect("device_cgq", request);
		model.addAttribute("device_cgq", device_cgq);
		List sys_device_area = DictionaryUtils.setDicDataListSelect("sys_device_area", request);
		model.addAttribute("sys_device_area", sys_device_area);
		return "modules/sys/derivce/device/deviceForm";
	}

	/**
	 * 查看编辑设备
	 */
	@RequestMapping(value = "getDeviceInfo")
	@ResponseBody
	public JSONObject getDeviceInfo(HttpServletRequest request, Map map) {
		JSONObject device = (JSONObject) map.get("map");
		return device;
	}

	/**
	 * 查看设备
	 */
	@RequestMapping(value = "read")
	public String read(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject device = (JSONObject) map.get("map");
		model.addAttribute("device",device);
		List sys_device_ctl = DictionaryUtils.setDicDataListSelect("sys_device_ctl", request);
		model.addAttribute("sys_device_ctl", sys_device_ctl);
		List sys_device_use = DictionaryUtils.setDicDataListSelect("sys_device_use", request);
		model.addAttribute("sys_device_use", sys_device_use);
		List office_unit = DictionaryUtils.setDicDataListSelect("office_unit", request);
		model.addAttribute("office_unit", office_unit);
		//代码参数类型
		List field = DictionaryUtils.setDicDataList("sys_field_fieldClass", request);
		model.addAttribute("field", field);
		List device_type = DictionaryUtils.setDicDataListSelect("device_type", request);
		model.addAttribute("device_type", device_type);

		List device_cgq = DictionaryUtils.setDicDataListSelect("device_cgq", request);
		model.addAttribute("device_cgq", device_cgq);
		List sys_device_area = DictionaryUtils.setDicDataListSelect("sys_device_area", request);
		model.addAttribute("sys_device_area", sys_device_area);
		return "modules/sys/derivce/device/deviceRead";
	}

	/**
	 * 保存设备
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/device/saveAndUpdate");
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
	 * 停用设备
	 */
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/device/disable");
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
	 * 启用设备
	 */
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/enable");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return renderResult(Global.TRUE, data.getMessage());
	}

	/**
	 * 删除设备
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/delete");
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
	 * 启用设备
	 */
	@RequestMapping(value = "dic")
	@ResponseBody
	public String dic(HttpServletRequest request,String dickey) {
		String sys_field_fieldClass = DictionaryUtils.setDicDataId(dickey, request);
		return sys_field_fieldClass;
	}

	/**
	 * 获取代码值
	 */
	@RequestMapping(value = "paramValus")
	@ResponseBody
	public String paramValus(HttpServletRequest request,String dickey) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/paramValus");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();

	}

	/**
	 * 获取机构树结构数据
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public String treeData(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/treeData");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	/**
	 * 获取机构树结构数据（根据权限）
	 * @return
	 */
	@RequestMapping(value = "treeDataT")
	@ResponseBody
	public String treeDataT(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/treeDataT");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	/**
	 *根据人员id返回基础信息
	 * @return
	 */
	@RequestMapping(value = "basics")
	@ResponseBody
	public String basics(HttpServletRequest request,String userCode) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/basics"+ userCode);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}
	/**
	 * 获取测点次数
	 */
	@PostMapping(value = "cedianbilie")
	@ResponseBody
	public String cedianbilie(HttpServletRequest request) {
		Result data = null;
		data = OALoginUtiles.dataGet(request, null, "/deviceQuery/findIsHavePointTopList");
		if (data.getCode() == 20000) {
			return data.getData();
		} else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}


	}
	/**
	 * 获取测点次数
	 */
	@PostMapping(value = "cedianbilieTo")
	@ResponseBody
	public String cedianbilieTo(HttpServletRequest request) {
		Result data = null;
		data = OALoginUtiles.dataGet(request, null, "/point/findAllByDeviceCodeTo");
		if (data.getCode() == 20000) {
			return data.getData();
		} else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}


	}
	/**
	 * 获取测点次数
	 */
	@PostMapping(value = "cedianbilieToK")
	@ResponseBody
	public String cedianbilieToK(HttpServletRequest request,String deviceCode) {
		Result data = null;
		data = OALoginUtiles.dataGet(request, null, "/point/findAllByDeviceCodeToK/"+deviceCode);
		if (data.getCode() == 20000) {
			return data.getData();
		} else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}


	}


	/**
	 * 获取设施设备树结构数据，包含名称和id
	 * @return
	 */
	@RequestMapping(value = "treeData2")
	@ResponseBody
	public String treeData2(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/treeData2");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 获取设施设备数据，只展示传感器为是的，且已录入传感器不再重复显示
	 * 展示直拉式列表，包含名称、编号和id
	 * @return
	 */
	@RequestMapping(value = "treeData3")
	@ResponseBody
	public String treeData3(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/device/treeData3");
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


/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.plan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 计划Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/subCentersPlan")
public class SubCentersController extends BaseController {

	/**
	 * 获取计划
	 */
	@ModelAttribute
	public Map get(String id, HttpServletRequest request) {
		if(id!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("id", id);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/subCentersPlan/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            if(data.getData()==null){
                return  null;
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	/**
	 * 设备计划
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request, String stationId) {
		Result data = null;
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("type","分局");
//			map.put("id","user15_mrr3");
			data = OALoginUtiles.authGet(request, map,"/office/orgOfficeTypeEnd");
			model.addAttribute("subType",JSONObject.parse(data.getData()));
			map.clear();
			map.put("typeValue","管理站/处");
//			map.put("id",JSONObject.parseObject(data.getData()).get("id"));
			data = OALoginUtiles.authGet(request, map,"/office/orgOfficeChirlds");
			model.addAttribute("engType",JSONObject.parse(data.getData()));
			model.addAttribute("stationId",stationId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/sys/plan/subcenters/planList";
	}

	/**
	 * 查询计划数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/subCentersPlan/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	/**
	 * 查询计划数据
	 */
	@RequestMapping(value = "listDataCL")
	@ResponseBody
	public String listDataCL(HttpServletRequest request) {
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/subCentersPlan/listDataCL");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 查看计划
	 */
	@RequestMapping(value = "form")
	public String form(String planParentId, String stationId, String stationName, HttpServletRequest request, String deviceCode, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		model.addAttribute("stationId",stationId);
		model.addAttribute("stationName",stationName);
		return "modules/sys/plan/subcenters/planForm";
	}

	/**
	 * 查看计划
	 */
	@RequestMapping(value = "planformLC")
	public String planformLC(HttpServletRequest request, String deptId, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		model.addAttribute("orgId",deptId);
		return "modules/sys/plan/subcenters/planFormLC";
	}
	/**
	 * 查看计划
	 */
	@RequestMapping(value = "planformLCB")
	public String planformLCB(HttpServletRequest request, String deptId, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		model.addAttribute("orgId",deptId);
		return "modules/sys/plan/subcenters/planformLCB";
	}

	/**
	 * 保存计划
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/subCentersPlan/saveAndUpdate");
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
	 * 删除计划
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/subCentersPlan/delete");
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
	 * 管理站审核状态修改
	 */
	@CrossOrigin
	@RequestMapping(value = "savePlanStatus")
	@ResponseBody
	public String savePlanStatus(HttpServletRequest request,String id,String status) {
		String jsonpCallback=request.getParameter("jsonpCallback");
		System.out.println("------------------------------------通过不通过-省局计划");
		Result data = null;
		Map map = new HashMap();
		map.put("id", id);
		map.put("status", status);

		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", map,"/subCentersPlan/savePlanStatus");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		if(jsonpCallback==null) {
			if(data.getCode()==20000) {
				return renderResult(Global.TRUE, text(data.getMessage()));
			}
			else {
				return renderResult(Global.FALSE, text(data.getMessage()));
			}
		}else {
			return jsonpCallback + "(" + data.getMessage() + ")";
		}
	}
	/**
	 * 修改计划
	 */
	@CrossOrigin
	@RequestMapping(value = "sdd")
	@ResponseBody
	public String savePlanStatus(String userName,HttpServletRequest request) {
		String jsonpCallback=request.getParameter("jsonpCallback");
//		String userName = request.getParameter("userName");
		System.out.println("------------------------------------通过不通过-省局计划");
		Result data = null;
		Map map = new HashMap();
		map.put("userName", userName);
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request), "/subCentersPlan/sdd/"+userName);
			System.out.println("------------------------------------通过不通过-1111111局计划");
//			System.out.println(data.getData());
//			System.out.println(data);
//			System.out.println(data.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
		String messge  = JSON.toJSONString(data.getData());
		return jsonpCallback + "(" + messge + ")";
//		return jsonpCallback+"("+messge+ ")";

	}

	/**
	 * 管理站上报状态修改
	 */
    @CrossOrigin(allowCredentials="true")
	@RequestMapping(value = "savePlanReport")
	@ResponseBody
	public String savePlanReport(HttpServletRequest request) {
        String jsonpCallback=request.getParameter("jsonpCallback");
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/subCentersPlan/savePlanReport");
		} catch (SystemException e) {
			e.printStackTrace();
		}
        if(jsonpCallback==null) {
            if(data.getCode()==20000) {
                return renderResult(Global.TRUE, text(data.getMessage()));
            }
            else {
                return renderResult(Global.FALSE, text(data.getMessage()));
            }
        }else {
            return jsonpCallback + "(" + data.getMessage() + ")";
        }
	}

	/**
	 * 查询列表
	 */
	@RequestMapping(value = "oa")
	public String oa(Model model) {
		model.addAttribute("oa",new Oa());
		return "modules/sys/plan/subcenters/todoApplication";
	}

	//计划代办申请
	@RequestMapping(value = "listDataOA")
	@ResponseBody
	public Page<Oa> listDataOA(HttpServletRequest request, HttpServletResponse response) {
		String pageNo = request.getParameter("pageNo")==null||request.getParameter("pageNo").equals("")?"1":request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize")==null||request.getParameter("pageSize").equals("")?"20":request.getParameter("pageSize");
		String orderBy = request.getParameter("orderBy");
		// 系统登陆
		String path = "horizon/workflow/rest/todo/page.wf?page="+pageNo+"&limit="+pageSize+"&sort="+orderBy;
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("requestType", "TodoList");
		dataMap.put("search","省局养护计划审核");
		dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
		Page<Oa> page = new Page<>();
		page.setList(OALoginUtiles.entityJson(reslt));

		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
		Object recordsTotal = jsonobject.get("recordsTotal");
		page.setCount(Long.parseLong((String) recordsTotal));
		page.setPageNo(Integer.parseInt(pageNo));
		page.setPageSize(Integer.parseInt(pageSize));
		return page;
	}
	//计划代办申请
	@RequestMapping(value = "listDataOAL")
	@ResponseBody
	public Page<Oa> listDataOAL(HttpServletRequest request, HttpServletResponse response) {

		// 系统登陆
		String path = "horizon/workflow/rest/todo/page.wf?page=1&limit=5&sort=";
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("requestType", "TodoList");
		dataMap.put("search","省局养护计划审核");
		dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
		Page<Oa> page = new Page<>();
		page.setList(OALoginUtiles.entityJson(reslt));
		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
		Object recordsTotal = jsonobject.get("recordsTotal");
		page.setCount(Long.parseLong((String) recordsTotal));
		return page;
	}

	//计划已办申请
	@RequestMapping(value = "listDataOATo")
	@ResponseBody
	public Page<Oa> listDataOATo(HttpServletRequest request, HttpServletResponse response) {
//		String pageNo = request.getParameter("pageNo")==null||request.getParameter("pageNo").equals("")?"1":request.getParameter("pageNo");
//		String pageSize = request.getParameter("pageSize")==null||request.getParameter("pageSize").equals("")?"20":request.getParameter("pageSize");
//		String orderBy = request.getParameter("orderBy");
		// 系统登陆
		String path = "horizon/workflow/rest/haddone/page.wf?page=1&limit=5&sort=";
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("requestType", "HadDone");
		dataMap.put("search","省局养护计划审核");
		dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
		Page<Oa> page = new Page<>();
		page.setList(OALoginUtiles.entityJson(reslt));
		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
		Object recordsTotal = jsonobject.get("recordsTotal");
		page.setCount(Long.parseLong((String) recordsTotal));
		return page;
	}
}

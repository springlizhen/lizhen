/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.plan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/plan")
public class PlanController extends BaseController {

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
                data = OALoginUtiles.sysGet(request,"systemhttp", dataMap,"/plan/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
		}
		return null;
	}
	/**
	 * 设备计划
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		Result data = null;
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("type","分局");
//			map.put("id","user15_mrr3");
			data = OALoginUtiles.authGet(request, map,"/office/orgOfficeTypeEnd");
			model.addAttribute("subType",JSONObject.parse(data.getData()));
			map.clear();
			map.put("type","管理站/处");
//			map.put("id","user15_mrr3");
			data = OALoginUtiles.authGet(request, map,"/office/orgOfficeTypeEnd");
			model.addAttribute("engType",JSONObject.parse(data.getData()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/sys/plan/planList";
	}

	/**
	 * 查询计划数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/plan/pageList");
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
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/plan/listDataCLSub");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return data.getData();
	}

	/**
	 * 查看计划
	 */
	@RequestMapping(value = "form")
	public String form(String stationId, String stationName, String planParentName, String planParentId, HttpServletRequest request, String deviceCode, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		model.addAttribute("stationId",stationId);
		model.addAttribute("stationName",stationName);
		model.addAttribute("planParentName",planParentName);
		model.addAttribute("planParentId",planParentId);
		return "modules/sys/plan/planForm";
	}

	/**
	 * 查看计划
	 */
	@RequestMapping(value = "planFormLC")
	public String planFormLC(HttpServletRequest request, String deviceCode, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		return "modules/sys/plan/planFormLC";
	}

	/**
	 * 查看计划
	 */
	@RequestMapping(value = "see")
	public String see(HttpServletRequest request, String deviceCode, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject sensor = (JSONObject) map.get("map");
		model.addAttribute("plan",sensor);
		return "modules/sys/plan/planFormSee";
	}

	/**
	 * 保存计划
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;

        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/plan/saveAndUpdate");
        } catch (SystemException e) {
            e.printStackTrace();
        }
		String message = data.getMessage();
		String[] split = message.split(",");
		BASE64Decoder decoder = new BASE64Decoder();
		String object = "";
		if(split.length>1) {
			try {
				byte[] bytes = decoder.decodeBuffer(split[1]);
				object = new String(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONObject s1 = JSONObject.parseObject(object);
		s1.put("messages", split[0]);
		if(data.getCode()==20000) {
			return renderResult(Global.TRUE, s1.toJSONString());
		}else{
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
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/plan/delete");
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
	@RequestMapping(value = "savePlanStatus")
	@ResponseBody
	@CrossOrigin
	public String savePlanStatus(HttpServletRequest request, String id, String status) {
		String jsonpCallback=request.getParameter("jsonpCallback");
		System.out.println("------------------------------------通过不通过-分局计划");
		Result data = null;
		Map map = new HashMap();
		map.put("id", id);
		map.put("status", status);
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", map,"/plan/savePlanStatus");
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
	 * 管理站上报状态修改
	 */
    @CrossOrigin(allowCredentials="true")
	@RequestMapping(value = "savePlanReport")
	@ResponseBody
	public String savePlanReport(HttpServletRequest request) {
        String jsonpCallback=request.getParameter("jsonpCallback");
		Result data = null;
		try {
			data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/plan/savePlanReport");
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
		return "modules/sys/plan/todoApplication";
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
		dataMap.put("search","分局养护计划审核");
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
	//所有申请的数量
	@RequestMapping(value = "listDataOAL")
	@ResponseBody
	public Page<Oa> getHomePageAmount(HttpServletRequest request, HttpServletResponse response){
		String path = "horizon/workflow/rest/todo/page.wf?page=1&limit=5&sort=";
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("requestType", "TodoList");
		dataMap.put("search","分局养护计划审核");
		dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
		Page<Oa> page = new Page<>();
		page.setList(OALoginUtiles.entityJson(reslt));
		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
		Object recordsTotal = jsonobject.get("recordsTotal");
		page.setCount(Long.parseLong((String) recordsTotal));
		return page;

	}

	//计划代办申请
	@RequestMapping(value = "listDataCount")
	@ResponseBody
	public List<Page<Oa>> listDataOAL(HttpServletRequest request, HttpServletResponse response) {
		// 系统登陆
		List<Page<Oa>> list = new ArrayList<>();
		String path = "horizon/workflow/rest/todo/page.wf?page=1&limit=5&sort=";
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("requestType", "TodoList");
		dataMap.put("search","分局养护计划审核");
		dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
		Page<Oa> page = new Page<>();
		page.setList(OALoginUtiles.entityJson(reslt));
		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
		Object recordsTotal = jsonobject.get("recordsTotal");
		String path1 = "horizon/workflow/rest/haddone/page.wf?page=1&limit=5&sort=";
		Map dataMapTo = new HashMap<String, Object>();
		dataMapTo.put("requestType", "HadDone");
		dataMapTo.put("search","分局养护计划审核");
		dataMapTo.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String reslt1 = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path1, net.sf.json.JSONObject.fromObject(dataMapTo));
		Page<Oa> page1 = new Page<>();
		page1.setList(OALoginUtiles.entityJson(reslt));
		net.sf.json.JSONObject jsonobject1 = net.sf.json.JSONObject.fromObject(reslt1);
		Object recordsTotal2 = jsonobject1.get("recordsTotal");
		page1.setCount(Long.parseLong((String) recordsTotal2));
		page.setCount(Long.parseLong((String) recordsTotal)+Long.parseLong((String) recordsTotal2));
		// 系统登陆
		String path2 = "horizon/workflow/rest/todo/page.wf?page=1&limit=5&sort=";
		Map dataMap2 = new HashMap<String, Object>();
		dataMap2.put("requestType", "TodoList");
		dataMap2.put("search","省局养护计划审核");
		dataMap2.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String resltTo = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path2, net.sf.json.JSONObject.fromObject(dataMap2));
		Page<Oa> page3 = new Page<>();
		page.setList(OALoginUtiles.entityJson(resltTo));
		net.sf.json.JSONObject jsonobject3 = net.sf.json.JSONObject.fromObject(resltTo);
		Object recordsTotal3 = jsonobject3.get("recordsTotal");
		String path3 = "horizon/workflow/rest/haddone/page.wf?page=1&limit=5&sort=";
		Map dataMap3 = new HashMap<String, Object>();
		dataMap3.put("requestType", "HadDone");
		dataMap3.put("search","省局养护计划审核");
		dataMap3.put("accessToken", new OALoginUtiles().tokenNew(request, response));
		String resltp = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path3, net.sf.json.JSONObject.fromObject(dataMap3));
		Page<Oa> pagep = new Page<>();
		page.setList(OALoginUtiles.entityJson(resltp));
		net.sf.json.JSONObject jsonobject9 = net.sf.json.JSONObject.fromObject(resltp);
		Object recordsTotal7 = jsonobject9.get("recordsTotal");
		page3.setCount(Long.parseLong((String) recordsTotal7)+Long.parseLong((String) recordsTotal3));
		list.add(page);
		list.add(page1);
		list.add(page3);
		return list;
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
		dataMap.put("search","分局养护计划审核");
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

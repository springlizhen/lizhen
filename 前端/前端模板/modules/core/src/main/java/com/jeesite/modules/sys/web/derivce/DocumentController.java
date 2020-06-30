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
import java.util.Map;

/**
 * 分类Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/document")
public class DocumentController extends BaseController {



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
		data = OALoginUtiles.dataGet(request,dataMap,"/document/selectDocument");
		return (Map) JSON.parse(data.getData());
		}


	/**
	 * 分类列表
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/document/documentList";
	}
	
	/**
	 * 查询分类数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/document/select");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
	}

	@RequestMapping(value = "listView")
	public String listView(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject document = (JSONObject) map.get("map");
		model.addAttribute("document",document);
		return "modules/sys/derivce/document/documentViewList";
	}
	@RequestMapping(value = "view")
	public String view(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject document = (JSONObject) map.get("map");
		model.addAttribute("document",document);
		return "modules/sys/derivce/document/documentView";
	}

	/**
	 * 查看编辑分类
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		// 创建并初始化下一个节点信息
		JSONObject document = (JSONObject) map.get("map");
		model.addAttribute("document",document);
		return "modules/sys/derivce/document/documentForm";
	}

	/**
	 * 保存分类
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/document/save");
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
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/document/delete");
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

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.derivce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.image.ZxingUtils;
import com.jeesite.common.utils.QRCodeGenerator;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;

/**
 * 分类Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/check")
public class CheckController extends BaseController {

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
		data = OALoginUtiles.dataGet(request,dataMap,"/check/selectCheck");
		return (Map) JSON.parse(data.getData());
		}
	@RequestMapping(value = "listCheckNote")
	public String listCheckNote(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/checkNote";
	}

	@RequestMapping(value = "listCheck1")
	public String listCheck1(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/check1";
	}@RequestMapping(value = "listCheck2")
	public String listCheck2(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/check2";
	}@RequestMapping(value = "listCheck3")
	public String listCheck3(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/check3";
	}
	@RequestMapping(value = "listCheck")
	public String listCheck(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/check";
	}

	@RequestMapping(value = "checkImage")
	public String checkImage(Model model, HttpServletRequest request) {
		return "modules/sys/derivce/check/image";
	}

	@GetMapping(value="image",produces = IMAGE_JPEG_VALUE )
	@ResponseBody
	public void image(Model model, HttpServletRequest request, HttpServletResponse response,Map map) {
		JSONObject jsonObject =(JSONObject)map.get("map");
		String id=jsonObject.getString("id");
		String longitude=jsonObject.getString("longitude");
		String latitude=jsonObject.getString("latitude");
				BufferedImage image;
				// 禁止图像缓存
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("image/jpeg");
		//String content="https://www.baidu.com/";

		String content="http://192.168.1.60:9010/js/a/sys/check/form?id="+id;
		//String content="id:"+id+"longitude:"+longitude+"latitude:"+latitude;

				image = QRCodeGenerator.createImage(content);
				// 创建二进制的输出流
				try(ServletOutputStream sos = response.getOutputStream()){
					// 将图像输出到Servlet输出流中。
					ImageIO.write(image, "jpeg", sos);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		manageStation.put("officeLevel","管理站/处");
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


		return "modules/sys/derivce/check/checkList";
	}
	
	/**
	 * 查询分类数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/check/select");
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
		return "modules/sys/derivce/check/checkView";
	}

	/**
	 * 查看编辑分类
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Model model,Map map) {
		List<Map<String,Object>> list=null;
		Result data=null;
		Map dataMap = MapUtils.newHashMap();
		Map manageStation = MapUtils.newHashMap();
		Map manageOffice = MapUtils.newHashMap();
		manageOffice.put("officeLevel","管理所");
		manageStation.put("officeLevel","管理站/处");
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
		// 创建并初始化下一个节点信息
		JSONObject standard = (JSONObject) map.get("map");
		model.addAttribute("check",standard);
		return "modules/sys/derivce/check/checkForm";
	}

	/**
	 * 保存分类
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/check/save");
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
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/check/delete");
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

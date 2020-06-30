/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.utils.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hyperic.sigar.ProcCred;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.service.UserService;

/**
 * 员工用户Controller
 * @author ThinkGem
 * @version 2017-03-26
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/empUser")
public class EmpUserController extends BaseController {

	@Autowired
	private EmpUserService empUserService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private UserService userService;

	@ModelAttribute
	public Map get(String userCode, HttpServletRequest request) {
		if(userCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("userCode", userCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/empUser/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

	@RequestMapping(value = "index")
	public String index() {
		return "modules/sys/user/empUserIndex";
	}

	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, Model model) {
		// 获取岗位列表
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/allList");
		List<Post> j = JSONArray.parseArray(data.getData(),Post.class);
		model.addAttribute("postList", j);
		String sys_status = DictionaryUtils.setDicData("sys_status", request);
		model.addAttribute("sys_status", sys_status);
		return "modules/sys/user/empUserList";
	}

	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/empUser/pageList");
		return data.getData();
	}

	@RequestMapping(value = "form")
	public String form(String op, Model model, HttpServletRequest request, Map map) {
		JSONObject empUser = (JSONObject) map.get("map");
		// 设置默认的部门
//		if (StringUtils.isBlank(employee.getCompany().getCompanyCode())) {
//			employee.setCompany(EmpUtils.getCompany());
//		}

		// 设置默认的公司
//		if (StringUtils.isBlank(employee.getOffice().getOfficeCode())) {
//			employee.setOffice(EmpUtils.getOffice());
//		}

		// 获取岗位列表
		Result postList = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/post/allList");
		List<Post> j = JSONArray.parseArray(postList.getData(),Post.class);
		model.addAttribute("postList", j);

		// 获取当前用户所拥有的岗位
//		if (StringUtils.isNotBlank(employee.getEmpCode())){
//			employee.setEmployeePostList(employeeService.findEmployeePostList(employee));
//		}

		// 获取当前编辑用户的角色和权限
		if (StringUtils.inString(op, Global.OP_AUTH)) {

			// 获取当前用户所拥有的角色
			Result roleList = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/empUser/roleList");
			List<Role> r = JSONArray.parseArray(roleList.getData(),Role.class);
			model.addAttribute("roleList", r);

		}
		// 操作类型：add: 全部； edit: 编辑； auth: 授权
		model.addAttribute("op", op);

		model.addAttribute("empUser",empUser);
		return "modules/sys/user/empUserForm";
	}

	@RequestMapping({"checkLoginCode"})
	@ResponseBody
	public String checkLoginCode(String oldLoginCode, String loginCode, HttpServletRequest request) {
		User a;
		(a = new User()).setLoginCode(loginCode);
		if (loginCode != null && loginCode.equals(oldLoginCode)) {
			return "true";
		} else {
			Result data = OALoginUtiles.authGet(request,RequestParameter.requestParamMap(request),"/empUser/checkLoginCode");
			return loginCode != null && data.getData().equals("true") ? "true" : "false";
		}
	}

	@PostMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response) {
//		if (User.isSuperAdmin(empUser.getUserCode())) {
//			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
//		}
//		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
//			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
//		}
//		if (!Global.TRUE.equals(userService.checkLoginCode(oldLoginCode, empUser.getLoginCode()/*, null*/))) {
//			return renderResult(Global.FALSE, text("保存用户失败，登录账号''{0}''已存在", empUser.getLoginCode()));
//		}
//		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_EDIT)
//				&& UserUtils.getSubject().isPermitted("sys:empUser:edit")){
//			empUserService.save(empUser);
////			String token = OALoginUtiles.getToken(request,response);
////			OAUserUtils.oaUserUtils().addUser(token,empUser);
//		}
//		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_AUTH)
//				&& UserUtils.getSubject().isPermitted("sys:empUser:authRole")){
//			userService.saveAuth(empUser);
//		}
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMapPost(request),"/empUser/saveAndUpdate");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
//		return renderResult(Global.TRUE, text("保存用户''{0}''成功", empUser.getUserName()));
	}

	/**
	 * 停用用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "disable")
	public String disable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/empUser/disable");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 启用用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "enable")
	public String enable(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/empUser/enable");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 密码重置
	 * @return
	 */
	@RequestMapping(value = "resetpwd")
	@ResponseBody
	public String resetpwd(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/empUser/resetpwd");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 删除用户
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/empUser/delete");
		if (data.getCode()==20000) {
			OAUserUtils.oaSyn();
			return renderResult(Global.TRUE, text(data.getMessage()));
		}
		else{
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	/**
	 * 选择员工对话框
	 */
	@RequestMapping(value = "empUserSelect")
	public String empUserSelect(EmpUser empUser, String selectData, Model model, HttpServletRequest request) {
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (JsonMapper.fromJson(selectDataJson, Map.class) != null){
			model.addAttribute("selectData", selectDataJson);
		}
		String sys_status = DictionaryUtils.setDicData("sys_status", request);
		model.addAttribute("sys_status", sys_status);
		model.addAttribute("empUser", empUser);
		return "modules/sys/user/empUserSelect";
	}

}

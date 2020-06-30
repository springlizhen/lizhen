package com.chinags.auth.controller;

import com.chinags.auth.entity.*;
import com.chinags.auth.entity.Base64;
import com.chinags.auth.service.*;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.io.FileUtils;
import com.chinags.common.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Api("用户管理controller")
@RestController
@CrossOrigin
@RequestMapping("/empUser")
public class SysUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private CommService commService;

    /**
     * 分页获取用信息
     * @return
     */
    @ApiOperation("分页获取用户信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result userListPage(SysUser sysUser){
        PageResult<Office> page;
        try {
            //类似查询全部
            page = sysUserService.find(sysUser);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("用户树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(){
        List list = ListUtils.newArrayList();

        try {

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        Office office = new Office();
        List<Office> all = officeService.findAll(office);


        for (Office o : all){
            HashMap map = MapUtils.newHashMap();
            map.put("id", o.getOfficeCode());
            map.put("name", o.getOfficeName());
            map.put("isParent", true);
            map.put("pId", o.getParentCode());
            list.add(map);
            List<Map> users = sysUserService.findUsers(o.getOfficeCode());
            //放入用户
            for (Map s : users) {
                HashMap mapUser = MapUtils.newHashMap();
                mapUser.put("id", s.get("loginCode"));
                mapUser.put("name", s.get("userName"));
                mapUser.put("pId", s.get("officeCode"));
                list.add(mapUser);
            }
        }

        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 获取岗位信息
     * @return
     */
    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result userForm(String userCode){
        SysUser sysUser = new SysUser();
        try {
            if(StringUtils.isNotEmpty(userCode)) {
                sysUser = sysUserService.getSysUserByUserCode(userCode);
                Employee employee = sysUser.getEmployee();
                Set<Post> posts = employee.getPostSet();
                if(posts.size()>0) {
                    String postCode = "";
                    for (Post p : posts) {
                        postCode += p.getPostCode() + ",";
                    }
                    employee.setPostCode(postCode.substring(0, postCode.length() - 1));
                }
                sysUser.setEmployee(employee);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",sysUser);
    }

    /**
     * 修改用户信息
     * @return
     */
    @ApiOperation("修改用户信息")
    @RequestMapping(value = "/editUserInfo", method = RequestMethod.GET)
    public Result editUserInfo(String userCode, String email, String mobile, String phone, String sign, String userName, String sex){
        try {
            SysUser sysUser = sysUserService.getSysUserByUserCode(userCode);
            sysUser.setEmail(email);
            sysUser.setMobile(mobile);
            sysUser.setPhone(phone);
            sysUser.setSign(sign);
            if (StringUtils.isNotBlank(userName)) {
                sysUser.setUserName(userName);
            }
            if (StringUtils.isNotBlank(sex)) {
                sysUser.setSex(sex);
            }
            sysUserService.save(sysUser);
            return new Result(true, StatusCode.OK, "修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改失败");
        }
    }

    @ApiOperation("登录账号是否存在")
    @RequestMapping(value = "/checkLoginCode", method = RequestMethod.GET)
    public Result checkLoginCode(String loginCode){
        Boolean b;
        try{
            b = sysUserService.findCountUserCode(loginCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",b);
    }

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/loginCode", method = RequestMethod.GET)
    public Result loginCode(String loginCode){
        SysUser sysUser = new SysUser();
        try {
            if(StringUtils.isNotEmpty(loginCode)) {
                sysUser = sysUserService.getSysUserByLoginCode(loginCode);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",sysUser);
    }

    /**
     * 获取用戶权限
     * @return
     */
    @ApiOperation("获取用戶权限")
    @RequestMapping(value = "/roleList", method = RequestMethod.GET)
    @ResponseBody
    public Result roleList(String userCode){
        SysUser sysUser;
        try {
            if(StringUtils.isNotEmpty(userCode)) {
                sysUser = sysUserService.getSysUserByUserCode(userCode);
            }else{
                sysUser = sysUserService.getSysUserByUserCode(getLoginUser().getUsercode());
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        if(sysUser==null){
            return new Result(false, StatusCode.ERROR, "请指定用户");
        }
        //剔除已删除角色
        Set<Role> roleSet = sysUser.getRoleSet();
        for (Role r : roleSet) {
            if (r.getStatus().equals(Role.STATUS_DELETE)){
                roleSet.remove(r);
            }
        }
        sysUser.setRoleSet(roleSet);
        return new Result(true, StatusCode.OK, "获取成功",sysUser.getRoleSet());
    }

    @ApiOperation("保存用户")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody SysUser sysUser) {
        SysUser sysParent;
        sysParent = sysUserService.getSysUserByUserCode(sysUser.getUserCode());
        if(sysUser.getIsNewRecord()) {
            sysUser.setStatus("0");
            sysUser.setCorpCode("0");
            sysUser.setCorpName("JeeSite");
            if(sysParent!=null){
                return new Result(true, StatusCode.ERROR, "登录账号已存在");
            }
            String uuid = "_"+ UUID.randomUUID().toString().substring(0,4);
            String userCode = sysUser.getLoginCode() + uuid;  //用户id

            //employee表中的empCode（该字段为主键）和user表中的refCode（员工编号）一致，当添加用户时，如果不写员工编号，默认使用userCode为该字段的值，
            //而userCode的值为填写的loginCode+下划线+4位随机字符。empCode不可重复，如果重复结束方法并给与提示。
            //获取empcode
            String empCode = sysUser.getEmployee().getEmpCode();
            //没有填写empCode，使用拼接成的userCode
            if(StringUtils.isEmpty(empCode)){
                empCode = userCode;
            }
            Employee e = sysUser.getEmployee();
            e.setEmpCode(userCode);
            e.setCorpCode("0");
            e.setCorpName("JeeSite");
            e.setStatus("0");
            e.setCreateBy(getLoginUser().getUsercode());
            e.setCreateDate(new Date());
            e.setUpdateBy(getLoginUser().getUsercode());
            e.setUpdateDate(new Date());
            if(e.getPostCode()!=null) {
                Set<Post> posts = postService.inPostCode(e.getPostCode());
                e.setPostSet(posts);
            }
            sysUser.setEmployee(e);
            sysUser.setUserCode(userCode);
            sysUser.setPassword(PasswordUtils.encryptPassword(Global.getPassWord()));

            String refName = e.getEmpName();
            if(StringUtils.isEmpty(refName)){
                refName = sysUser.getUserName();
            }
            sysUser.setRefName(refName);
            sysUser.setMgrType("0");
            sysUser.setPwdSecurityLevel(0);
            sysUser.setUserWeight(sysUser.getUserWeight()==null?0:sysUser.getUserWeight());
        } else if(sysUser.getOp().equals(SysUser.OP_AUTH)){
            sysParent.setSex(sysUser.getSex());
            sysParent.setBirthday(sysUser.getBirthday());
            sysParent.setUserRoleString(sysUser.getUserRoleString());
            sysParent.setOp(SysUser.OP_AUTH);
            sysUser = sysParent;
        } else{
            sysUser.setRoleSet(sysParent.getRoleSet());
            sysUser.setStatus(sysParent.getStatus());
            sysUser.setCorpCode(sysParent.getCorpCode());
            sysUser.setCorpName(sysParent.getCorpName());
            sysUser.setRefName(sysParent.getRefName());
            sysUser.setMgrType(sysParent.getMgrType());
            sysUser.setPassword(sysParent.getPassword());
            sysUser.setPwdSecurityLevel(sysParent.getPwdSecurityLevel());
            Employee e = sysUser.getEmployee();
            e.setCorpCode(sysParent.getEmployee().getCorpCode());
            e.setCorpName(sysParent.getEmployee().getCorpName());
            e.setStatus(sysParent.getEmployee().getStatus());
            e.setUpdateBy(getLoginUser().getUsercode());
            if(e.getPostCode()!=null) {
                Set<Post> post = postService.inPostCode(e.getPostCode());
                e.setPostSet(post);
            }
            sysUser.setEmployee(e);
            sysUser.setRefName(e.getEmpName());
        }
        if(sysUser.getOp().equals(SysUser.OP_AUTH)||sysUser.getIsNewRecord()){
            String userRoleString = sysUser.getUserRoleString();
            //创建用户时没有勾选角色不予保存
            if(StringUtils.isNotEmpty(userRoleString)){
                Set<Role> roles = roleService.inRoleCode(sysUser.getUserRoleString());
                sysUser.setRoleSet(roles);
            }
        }
        sysUser.setCreateBy(getLoginUser().getUsercode());
        sysUser.setUpdateBy(getLoginUser().getUsercode());
        sysUser.setCreateDate(new Date());
        sysUser.setUpdateDate(new Date());
        this.sysUserService.save(sysUser);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("重置密码")
    @RequestMapping(value = "/resetpwd", method = RequestMethod.GET)
    @ResponseBody
    public Result resetpwd(String userCode) {
        sysUserService.resetpwd(userCode);
        SysUser sysUser = sysUserService.getSysUserByUserCode(userCode);
        try {
            String password =  Global.getPassWord();
            String pw = PasswordUtils.encryptPassword(password);
            // 生成Token
            String token = JwtUtil.createJWTPassword(sysUser.getUserCode(), sysUser.getUserName(), "password", pw, sysUser.getLoginCode());
            // 从oa更新密码
            Comm oa = commService.getCommByAuthCode("default");
            HttpUtil.sendGet(oa.getAuthUrl().replace("logins", "editPassword"), "userInfo=" + token);
        } catch (Exception e) {
            logger.warn("=========----------------=========", e);
        }
        return new Result(true, StatusCode.OK, "重置用户\""+sysUser.getUserName()+"\"密码成功");
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String userCode) {
        SysUser sysUser = sysUserService.delete(userCode);
        if(sysUser!=null) {
            return new Result(true, StatusCode.OK, "删除用户\""+sysUser.getUserName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "删除用户\""+sysUser.getUserName()+"\"失败");
        }
    }

    @ApiOperation("停用用户")
    @ResponseBody
    @RequestMapping(value = "disable")
    public Result disable(String id) {
        SysUser sysUser = sysUserService.status(id,"2");
        if(sysUser!=null) {
            return new Result(true, StatusCode.OK, "停用用户\""+sysUser.getUserName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "停用用户\""+sysUser.getUserName()+"\"失败");
        }
    }

    @ApiOperation("启用用户")
    @ResponseBody
    @RequestMapping(value = "enable")
    public Result enable(String id) {
        SysUser sysUser = sysUserService.status(id,"0");
        if(sysUser!=null) {
            return new Result(true, StatusCode.OK, "启用用户\""+sysUser.getUserName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "启用用户\""+sysUser.getUserName()+"\"失败");
        }
    }

    @ApiOperation("用户头像上传")
    @PostMapping("/uploadUserAvatar")
    @ResponseBody
    public Result uploadUserAvatar(@RequestBody Base64 base64) throws IOException {

        String username = base64.getUserName();
        String userCode = base64.getUserCode();
        String imgBase64 = base64.getImgBase64();

        String excension = "";
        if (imgBase64.contains("data:image/jpeg;base64,")) {
            excension = "jpeg";
            imgBase64 = imgBase64.replace("data:image/jpeg;base64,", "");
        } else if (imgBase64.contains("data:image/png;base64,")) {
            excension = "png";
            imgBase64 = imgBase64.replace("data:image/png;base64,", "");
        } else if (imgBase64.contains("data:image/jpg;base64,")) {
            excension = "jpg";
            imgBase64 = imgBase64.replace("data:image/jpg;base64,", "");
        }

        String savePath = "D:/Java/jeesite4/fileUpload";
        String fileName = "/userfiles/avatar/0/none/" + UUID.randomUUID() + "." + excension;
        String tempPath = "D:/fileUpload/temp/";
        String destPath = savePath + fileName;

        File f = new File(savePath + "/userfiles/avatar/0/none");
        if (!f.exists()) {
            f.mkdirs();
        }

        File tempFile = new File(tempPath + UUID.randomUUID() + "." + excension);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        try {

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decoderBytes = decoder.decodeBuffer(imgBase64);

            FileOutputStream fis = new FileOutputStream(tempFile);
            fis.write(decoderBytes);
            fis.close();

            File destFile = new File(destPath);
            FileUtils.moveFile(tempFile, destFile);

            // 更新用户avatar字段
            SysUser sysUser = sysUserService.getSysUserByUserCode(userCode);
            sysUser.setAvatar(fileName);
            sysUserService.save(sysUser);

            // 清除OA、DA缓存
            synchronizeOADA();

            // 返回图片地址
            Comm comm = commService.getCommByAuthCode("nwmh");
            String avatar = comm.getAuthUrl().replace("/a/portal/index", "") + sysUser.getAvatar();

            return new Result(true, StatusCode.OK, "上传成功", avatar);

        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "上传失败");
        } finally {
            FileUtils.deleteQuietly(tempFile);
        }

    }

    @ApiOperation("获取用户头像")
    @GetMapping("/getUserAvatar")
    @ResponseBody
    public Result getUserAvatar(String userCode) {
        try {
            SysUser sysUser = sysUserService.getSysUserByUserCode(userCode);
            Comm comm = commService.getCommByAuthCode("nwmh");
            if (StringUtils.isNotBlank(sysUser.getAvatar())) {
                String avatar = comm.getAuthUrl().replace("/a/portal/index", "") + sysUser.getAvatar();
                return new Result(true, StatusCode.OK, "获取成功", avatar);
            }
            return new Result(false, StatusCode.ERROR, "获取失败");
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
    }

    @Async
    public void synchronizeOADA() {
        try {
            // 从oa清除缓存
            Comm oa = commService.getCommByAuthCode("default");
            HttpUtil.sendGet(oa.getAuthUrl().replace("logins", "editPassword"), "userInfo=token");
            // 从da清除缓存
            Comm da = commService.getCommByAuthCode("archives");
            HttpUtil.sendGet(da.getAuthUrl().replace("logins", "editPassword"), "userInfo=token");
        } catch (Exception e) {
            logger.warn("SYNCHRONIZE_PASSWORD_[oa][da]", e);
        }
    }

}

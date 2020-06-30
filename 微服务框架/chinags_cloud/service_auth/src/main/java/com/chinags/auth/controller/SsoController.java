package com.chinags.auth.controller;

import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.LoginUser;
import com.chinags.auth.entity.UserPassword;
import com.chinags.auth.entity.SysUser;
import com.chinags.auth.service.CommService;
import com.chinags.auth.service.SysUserService;
import com.chinags.common.codec.EncodeUtils;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.lang.DateUtils;
import com.chinags.common.utils.HttpUtil;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.PasswordUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api("登录管理controller")
@Controller
@RequestMapping("/sso")
public class SsoController {

    protected final Logger logger = LoggerFactory.getLogger(SsoController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CommService commService;
    @RequestMapping("/token")
    @ResponseBody
    @CrossOrigin
    public Result token(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header!=null && !"".equals(header)){
            //如果有包含有Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")){
                //得到token
                String token = header.substring(7);
                try {
                    JwtUtil.parseJWT(token);
                    return new Result(true, StatusCode.OK, "保存成功");
                }catch (Exception e){
                    return new Result(true, StatusCode.TOKENNULLERROR, "token过期或超时");
                }
            }
        }
        return new Result(true, StatusCode.TOKENNULLERROR, "token过期或超时");
    }
    @RequestMapping("/login")
    @CrossOrigin
    public String login(Model model,String systemname) {
        model.addAttribute("systemname",systemname);
        model.addAttribute("OA",systemname);
        return "login";
    }
    @RequestMapping("/editPassword")
    @CrossOrigin
    public String editPassword(Model model, String token, String systemname) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(systemname)) {
            return "login";
        }
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String usercode = claims.getId();
            String username = claims.getSubject();
            String logincode = (String) claims.get("loginCode");
            model.addAttribute("systemname",systemname);
            model.addAttribute("usercode",usercode);
            model.addAttribute("username",username);
            model.addAttribute("logincode",logincode);
            return "editPassword";
        }catch (Exception e){
            return "login";
        }
    }
    @ApiOperation("修改密码")
    @CrossOrigin
    @RequestMapping(value="/editPassword", method = RequestMethod.POST)
    @ResponseBody
    public Result editPassword(String usercode, String oldPassword, String password, String confirmPassword) {

        SysUser sysUser = sysUserService.getSysUserByLoginCode(usercode);
        boolean b = PasswordUtils.validatePassword(oldPassword, sysUser.getPassword());
        if(!b) {
            return new Result(false, StatusCode.ERROR, "原密码错误");
        }
        if (StringUtils.isBlank(password)) {
            return new Result(false, StatusCode.ERROR, "新密码不能为空");
        }
        if (StringUtils.isBlank(confirmPassword)) {
            return new Result(false, StatusCode.ERROR, "请再次确认密码");
        }
        if (!confirmPassword.equals(password)) {
            return new Result(false, StatusCode.ERROR, "两次输入密码不一致");
        }

        // 校验密码
        Boolean isFlag = checkPassword(password);
        if (!isFlag) {
            return new Result(false, StatusCode.ERROR, "密码需大于8位，且包含大小写字母+数字+符号");
        }

        // 更新密文密码
        String pw = PasswordUtils.encryptPassword(password);
        sysUserService.editpwd(pw, usercode);

        // 清除OA、DA缓存
        synchronizeOADA();

        return new Result(true, StatusCode.OK, "修改成功");
    }

    @ApiOperation("修改密码返回登录URl")
    @CrossOrigin
    @RequestMapping(value="/loginEditPassword", method = RequestMethod.POST)
    @ResponseBody
    public Result loginEditPassword(@RequestBody UserPassword userPassword) {
        String usercode = userPassword.getUsercode();
        String username = userPassword.getUsername();
        String logincode = userPassword.getLogincode();
        String systemname = userPassword.getSystemname();
        String oldPassword = userPassword.getOldPassword();
        String password = userPassword.getPassword();
        String confirmPassword = userPassword.getConfirmPassword();
        SysUser sysUser = sysUserService.getSysUserByUserCode(usercode);
        boolean b = PasswordUtils.validatePassword(oldPassword, sysUser.getPassword());
        if(!b) {
            return new Result(false, StatusCode.ERROR, "原密码错误");
        }
        if (StringUtils.isBlank(password)) {
            return new Result(false, StatusCode.ERROR, "新密码不能为空");
        }
        if (StringUtils.isBlank(confirmPassword)) {
            return new Result(false, StatusCode.ERROR, "请再次确认密码");
        }
        if (!confirmPassword.equals(password)) {
            return new Result(false, StatusCode.ERROR, "两次输入密码不一致");
        }

        // 校验密码
        Boolean isFlag = checkPassword(password);
        if (!isFlag) {
            return new Result(false, StatusCode.ERROR, "密码需大于8位，且包含大小写字母+数字+符号");
        }

        // 更新密文密码
        String pw = PasswordUtils.encryptPassword(password);
        sysUserService.editpwd(pw, usercode);

        // 生成Token
        String token = JwtUtil.createJWTPassword(usercode, username, password, pw, logincode);

        // 清除OA、DA缓存
        synchronizeOADA();

        // 生成跳转链接
        Comm comm = commService.getCommByAuthCode(systemname);
        String systemUrl = comm.getAuthUrl() + "?token=" + token + "&userName=" + EncodeUtils.encodeUrl(username) + "&loginCode=" + logincode;
        logger.info("LOGIN_SUCCESS_[" + logincode + "][" + password + "][" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "][" + comm.getAuthCode() + "]");
        return new Result(true, StatusCode.OK, "修改成功", systemUrl);
    }
    @ApiOperation("登录")
    @CrossOrigin
    @RequestMapping(value="/ssologin", method = RequestMethod.POST)
    @ResponseBody
    public Result ssologin(LoginUser loginUser) {
        // 验证用户名是否正确
        SysUser sysUser = sysUserService.getSysUserByLoginCode(loginUser.getUsername());
        if(sysUser==null){
            return new Result(false, StatusCode.ERROR, "用户名或密码错误");
        }

        // 验证密码是否正确
        boolean b = PasswordUtils.validatePassword(loginUser.getPassword(), sysUser.getPassword());
        if(!b) {
            return new Result(false, StatusCode.ERROR, "用户名或密码错误");
        }

        // 校验系统
        Comm comm = commService.getCommByAuthCode(loginUser.getSystemname());
        if(comm==null){
            return new Result(false, StatusCode.ERROR, "没有当前系统");
        }

        // 生成Token
        String token = JwtUtil.createJWTPassword(sysUser.getUserCode(),sysUser.getUserName(),loginUser.getPassword(),sysUser.getPassword(),sysUser.getLoginCode());
        String systemUrl = comm.getAuthUrl() + "?token=" + token + "&userName=" + EncodeUtils.encodeUrl(sysUser.getUserName()) + "&loginCode=" + sysUser.getLoginCode();
        // 验证是否初始密码
        int isInitialPassword = 0;
        if (!loginUser.getPassword().equals("123456")) {
            logger.info("LOGIN_SUCCESS_[" + sysUser.getLoginCode() + "][" + loginUser.getPassword() + "][" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "][" + comm.getAuthCode() + "]");
        } else {
            isInitialPassword = 1;
        }
        // 返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("sysUser", sysUser);
        data.put("systemUrl", systemUrl);
        data.put("isInitialPassword", isInitialPassword);
        return new Result(true, StatusCode.OK, "登录成功", data);
    }

    @ApiOperation("登录")
    @RequestMapping(value="/phonelogin", method = RequestMethod.POST)
    @CrossOrigin
    @ResponseBody
    public Result phonelogin(@RequestBody LoginUser loginUser) {
        SysUser sysUser = sysUserService.getSysUserByLoginCode(loginUser.getUsername());
        String token = "";
        if (sysUser == null) {
            return new Result(false, StatusCode.ERROR, "用户名或密码错误！", token);
        }
        //验证密码是否正确
        boolean b = PasswordUtils.validatePassword(loginUser.getPassword(), sysUser.getPassword());
        if(!b){
            return new Result(false, StatusCode.ERROR, "用户名或密码错误！", token);
        }else{
            token = JwtUtil.createJWTPassword(sysUser.getUserCode(),sysUser.getUserName(),loginUser.getPassword(),sysUser.getPassword(),sysUser.getLoginCode());
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("token", token);
        map.put("user", sysUser);
        logger.info("LOGIN_SUCCESS_[" + sysUser.getLoginCode() + "][" + loginUser.getPassword() + "][" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "][mobile]");
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    /**
     * 清除OA。DA缓存
     */
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

    /**
     * 校验密码
     * @param password
     * @return
     */
    public Boolean checkPassword(String password) {
        Boolean flag = true;
        // 含有数字
        String CONTAIN_DIGIT_REGEX = ".*[0-9].*";
        // 包含小写字母
        String CONTAIN_LETTER_REGEX = ".*[a-z].*";
        // 包含大写字母
        String CONTAIN_D_REGEX = ".*[A-Z].*";
        String tmp = password;
        tmp = tmp.replaceAll("\\p{P}", "");
        if (!password.contains("+") && !password.contains("=")) {
            if (password.length() == tmp.length()) {
                flag = false;
            }
        }
        if (password.length() < 8 || !password.matches(CONTAIN_DIGIT_REGEX) || !password.matches(CONTAIN_LETTER_REGEX) || !password.matches(CONTAIN_D_REGEX)) {
            flag = false;
        }
        return flag;
    }

}

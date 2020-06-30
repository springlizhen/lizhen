package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.filter.FormAuthenticationFilter;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Base64;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.interceptor.Web;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.*;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 门户Controller
 *
 * @author suijinchi
 * @version 2020-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/portal")
public class PortalController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 门户首页
     */
    @GetMapping(value = "index")
    public String portal(HttpServletRequest request, HttpServletResponse response, Model model, String token) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        Claims claims;
        try {
            if(token!=null) {
                claims = JwtUtil.parseJWT(token);
                CookieUtils.setCookie(response, OALoginUtiles.cookieName, token);
                User user = new User();
                user.setUserName((String) claims.get("sub"));
                user.setLoginCode((String) claims.get("loginCode"));
                request.getSession().setAttribute(token, user);
                ServletUtils.redirectUrl(request,response,adminPath + "/portal/index");
                return null;
            }else{
                claims = JwtUtil.parseJWT(cookie);
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        //获取登录用户信息
        Object o = request.getSession().getAttribute(cookie);
        if(cookie==null){
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else if(o==null){
            ServletUtils.redirectUrl(request,response,adminPath + "/portal/index?token="+cookie);
            return null;
        }
        // 当前用户对象信息
        User sysUser = (User)request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        String params = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PARAMS_PARAM);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, user.getUserName());
        model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, user.getUserName());
        model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, params);
        model.addAttribute("token", cookie);
        model.addAttribute("user", user); // 设置当前用户信息
        model.addAttribute("OA", OAConfigUtil.getByName("oaHZhttp")+"workflow/manager/index.html?loginName="+claims.get("loginCode")+"&password="+claims.get("pm")+"&_version="+System.currentTimeMillis());

        //查询用户信息
        Map<String,Object> dataMap = new HashMap<>(1);
        dataMap.put("loginCode", claims.get("loginCode"));
        Result data = OALoginUtiles.authGet(request, dataMap,"/empUser/loginCode");
        model.addAttribute("sysUser", JSON.parse(data.getData()));

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));
        model.addAttribute("deviceUrl", NwmhConfigUtil.getByName("deviceUrl"));
        model.addAttribute("authUrl", NwmhConfigUtil.getByName("authUrl"));
        model.addAttribute("yunweiUrl", NwmhConfigUtil.getByName("yunweiUrl"));
        model.addAttribute("dbraUrl", NwmhConfigUtil.getByName("dbraUrl"));
        model.addAttribute("gisUrl", NwmhConfigUtil.getByName("gisUrl"));
        model.addAttribute("contractUrl", NwmhConfigUtil.getByName("contractUrl"));
        model.addAttribute("daUrl", NwmhConfigUtil.getByName("daUrl"));

        // 返回主页面视图
        return "modules/portal/index";
    }

    /**
     * 新闻中心
     */
    @GetMapping(value = "newsList")
    public String newsList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        model.addAttribute("token", cookie);

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));

        return "modules/portal/newsList";
    }

    /**
     * 新闻搜索列表
     */
    @GetMapping(value = "newsSearchList")
    public String newsSearchList(HttpServletRequest request, HttpServletResponse response, Model model, String articleTitle) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        model.addAttribute("articleTitle", articleTitle);
        model.addAttribute("token", cookie);

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));

        return "modules/portal/newsSearchList";
    }

    /**
     * 新闻详情
     */
    @GetMapping(value = "newsDetail")
    public String newsDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/article/form");
        model.addAttribute("article", JSON.parse(data.getData()));
        model.addAttribute("token", cookie);

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));

        return "modules/portal/newsDetail";
    }

    /**
     * 通知公告
     */
    @GetMapping(value = "newsTzggList")
    public String newsTzggList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        model.addAttribute("token", cookie);

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));

        return "modules/portal/newsTzggList";
    }

    /**
     * 下载中心
     */
    @GetMapping(value = "downloadList")
    public String downloadList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        model.addAttribute("token", cookie);

        // 系统地址
        model.addAttribute("oaUrl", NwmhConfigUtil.getByName("oaUrl"));

        // 下载地址
        model.addAttribute("shiyongshouce", NwmhConfigUtil.getByName("shiyongshouce"));
        model.addAttribute("wps", NwmhConfigUtil.getByName("wps"));
        model.addAttribute("chromeliulanqi", NwmhConfigUtil.getByName("chromeliulanqi"));

        return "modules/portal/downloadList";
    }

    /**
     * 个人中心
     */
    @RequestMapping("personalCenter")
    public String personalCenter(HttpServletRequest request, HttpServletResponse response, Model model) {

        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);

        User user = userService.getByLoginCode(sysUser);
        if (user == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        model.addAttribute("user", user);
        model.addAttribute("op", "base");

        return "modules/portal/personalCenter";
    }

    /**
     * 修改个人信息
     */
    @PostMapping("infoSaveBase")
    @ResponseBody
    public Result infoSaveBase(HttpServletRequest request, HttpServletResponse response, User user) {

        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);
        User uu = userService.getByLoginCode(sysUser);
        if (uu == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        try {
            userService.update(user);
            if (StringUtils.isNotBlank(user.getAvatarBase64())) {
                // 保存头像
                User u = userService.get(user);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("imgBase64", user.getAvatarBase64());
                dataMap.put("userCode", u.getUserCode());
                OALoginUtiles.authPost(request, dataMap,"/empUser/uploadUserAvatar");
            }
            return new Result(true, 20000, "修改成功");
        } catch (Exception e) {
            return new Result(false, 20001, "修改失败");
        }
    }

    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "password")
    @ResponseBody
    public Result password(HttpServletRequest request, HttpServletResponse response, String id, String oldPassword, String newPassword, String confirmNewPassword) {

        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User sysUser = (User) request.getSession().getAttribute(cookie);
        User uu = userService.getByLoginCode(sysUser);
        if (uu == null) {
            try {
                response.sendRedirect(Web.authPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        try {
            User u = new User();
            u.setId(id);
            User user = userService.get(u);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("systemname", "nwmh");
            dataMap.put("usercode", user.getUserCode());
            dataMap.put("username", user.getUserName());
            dataMap.put("logincode", user.getLoginCode());
            dataMap.put("oldPassword", oldPassword);
            dataMap.put("password", newPassword);
            dataMap.put("confirmPassword", confirmNewPassword);
            Result result = OALoginUtiles.authPost(request, dataMap, "/sso/loginEditPassword");
            if (result.isStatus()) {
                request.getSession().removeAttribute(cookie);
            }
            return result;
        } catch (Exception e) {
            return new Result(false, 20001, "修改失败");
        }
    }

}
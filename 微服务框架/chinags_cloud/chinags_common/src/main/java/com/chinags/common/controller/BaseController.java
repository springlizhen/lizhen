package com.chinags.common.controller;

import com.chinags.common.entity.LoginUser;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.ServletUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.Map;

public class BaseController {
    /**
     * 获取request
     */
    public HttpServletRequest getRequest()
    {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse()
    {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession()
    {
        return getRequest().getSession();
    }

    private Map<String,Boolean> userAuth;
    private LoginUser loginUser;

    public Boolean getUserAuth(String key) {
        Boolean val = userAuth.get(key);
        return val==null?false:val;
    }

    public LoginUser getLoginUser(){
        return this.loginUser;
    }

    @ModelAttribute
    public void setUserAuth() {
        String header = getRequest().getHeader("Authorization");
        if(header!=null && !"".equals(header)){
            //如果有包含有Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")){
                //得到token
                String token = header.substring(7);
                //对令牌进行验证
                try {
                    loginUser = new LoginUser();
                    Claims claims = JwtUtil.parseJWT(token);
                    loginUser.setUsercode(claims.getId());
                    loginUser.setUsername(claims.getSubject());
                    loginUser.setLogincode((String) claims.get("loginCode"));
                    String enu = getRequest().getParameter("systemname");
                    String method = getRequest().getMethod();
//                    //获取项目名称
                    String systemname = (String)getRequest().getAttribute("systemname");
                    loginUser.setSystemname(method.equals("GET")?enu:systemname);
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确！");
                }
            }
        }
        this.userAuth = userAuth;
    }

}

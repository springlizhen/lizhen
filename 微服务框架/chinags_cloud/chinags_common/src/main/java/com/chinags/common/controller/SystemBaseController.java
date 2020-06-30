package com.chinags.common.controller;

import com.chinags.common.entity.LoginUser;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.ServletUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class SystemBaseController {

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

    private String commCode;
    private String commName;

    public String getCommCode() {
        return commCode;
    }

    public String getCommName() {
        return commName;
    }

    @ModelAttribute
    public void setUserAuth() {
        String token = getRequest().getParameter("token");
        if(token!=null && !"".equals(token)){
            try {
                String appSecretKey = getRequest().getParameter("appSecretId");
                Claims claims = JwtUtil.parseJWTSystem(token,appSecretKey);
                this.commCode = claims.getId();
                this.commName = claims.getSubject();
                claims.getSubject();
            }catch (Exception e){
                throw new RuntimeException("令牌不正确！");
            }
        }
    }

}

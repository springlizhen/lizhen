package com.jeesite.modules.config.web;

import com.jeesite.modules.sys.interceptor.LoginWeb;
import com.jeesite.modules.sys.interceptor.Web;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Configuration
public class LoginWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginWeb())
                .addPathPatterns("/**");
        registry.addInterceptor(new Web())
                .addPathPatterns("/**");

    }
}

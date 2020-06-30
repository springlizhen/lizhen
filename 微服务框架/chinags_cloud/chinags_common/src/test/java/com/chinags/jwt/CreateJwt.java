package com.chinags.jwt;

import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.PasswordUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwt {
    public static void main(String[] arg){
//        JwtBuilder jwtBuilder = Jwts.builder()
//                .setId("1sdfa23412dfa")
//                .setSubject("测试")
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS256,"123123123")
//                .setExpiration(new Date(new Date().getTime()+3600000))
//                .claim("role","管理员");//自定义参数
        //密码生成
        String password = PasswordUtils.encryptPassword("123456");
        System.out.println(password);
        //密码对比是否正确
        Boolean b = PasswordUtils.validatePassword("123456", password);
        System.out.println(b);
    }
}

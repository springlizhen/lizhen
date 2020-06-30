package com.chinags.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator
 */
public class JwtUtil  {

    private static String secret="ceshi" ;

    private static long expires_in=432000000 ;//五天

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public static String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret).claim("roles", roles);
        if (expires_in > 0) {
            builder.setExpiration( new Date( nowMillis + expires_in));
        }
        return builder.compact();
    }
    public static String createJWT(String id, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret);
        if (expires_in > 0) {
            builder.setExpiration( new Date( nowMillis + expires_in));
        }
        return builder.compact();
    }

    public static String createJWTPassword(String id, String subject, String password) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret).claim("password",password);
        if (expires_in > 0) {
            builder.setExpiration( new Date( nowMillis + expires_in));
        }
        return builder.compact();
    }

    public static String createJWTPassword(String id, String subject, String password,String pm,String loginCode) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret).claim("password",password).claim("pm", pm).claim("loginCode", loginCode);
        if (expires_in > 0) {
            builder.setExpiration( new Date( nowMillis + expires_in));
        }
        return builder.compact();
    }

    public static Map<String,String> createJWTSystem(String id, String subject, String appSecretId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, appSecretId);
        if (expires_in > 0) {
            builder.setExpiration( new Date( nowMillis + expires_in));
        }
        Map<String,String> objectObjectMap = new HashMap<>();
        objectObjectMap.put("token",builder.compact());
        objectObjectMap.put("expires_in",(expires_in/1000)+"");
        return objectObjectMap;
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public static Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public static Claims parseJWTSystem(String jwtStr, String appSecretId){
        return  Jwts.parser()
                .setSigningKey(appSecretId)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}

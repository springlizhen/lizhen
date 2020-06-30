package com.chinags.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

public class ParseJwt {
    public static void main(String[] arg){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("123123123")
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxc2RmYTIzNDEyZGZhIiwic3ViIjoi5rWL6K-VIiwiaWF0IjoxNTYxMzM5NTUxLCJleHAiOjE1NjEzNDMxNTIsInJvbGUiOiLnrqHnkIblkZgifQ.W2D_tc-E_Tp4sIF1J47uhxFMm9qNbH-OTo7q1gGx9gg")
                    .getBody();
            System.out.println("用户id:" + claims.getId());
            System.out.println("用户名:" + claims.getSubject());
            System.out.println("登录时间:" + claims.getIssuedAt());
            System.out.println("用户角色:" + claims.get("role").toString());
        }catch (ExpiredJwtException eje){
            System.out.println("已过期:" + eje.getMessage());
        }catch (Exception e){
            System.out.println("错误:" + e.getMessage());
        }
    }
}

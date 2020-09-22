package com.github.hackyoma.frameexample.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author hackyo
 * @date 2018/8/22
 */
public final class JwtTokenUtil {

    public final static String TOKEN_HEADER = "Bearer ";
    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SIGNATURE_ALGORITHM);

    /**
     * 生成令牌
     *
     * @param username 用户名
     * @return 令牌
     */
    public static String generateToken(String username) {
        Date now = new Date();
        Claims claims = new DefaultClaims();
        claims.setSubject(username);
        claims.setExpiration(new Date(now.getTime() + 2592000L * 1000));
        claims.setNotBefore(now);
        return TOKEN_HEADER + Jwts.builder().setClaims(claims).signWith(SECRET_KEY, SIGNATURE_ALGORITHM).compact();
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public static String refreshToken(String token) {
        Date now = new Date();
        Claims claims = getClaims(token);
        claims.setExpiration(new Date(now.getTime() + 2592000L * 1000));
        claims.setNotBefore(now);
        return TOKEN_HEADER + Jwts.builder().setClaims(claims).signWith(SECRET_KEY, SIGNATURE_ALGORITHM).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaims(String token) {
        token = token.substring(TOKEN_HEADER.length());
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 从令牌中获取创建时间
     *
     * @param token 令牌
     * @return 创建时间
     */
    private static Date getCreatedTime(String token) {
        return getClaims(token).getNotBefore();
    }

    /**
     * 从令牌中获取过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    private static Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * 验证令牌有效性
     *
     * @param token                 令牌
     * @param lastPasswordResetTime 用户最后修改密码时间
     * @return 是否有效
     */
    public static Boolean validateToken(String token, Date lastPasswordResetTime) {
        return new Date().before(getExpiration(token)) && lastPasswordResetTime.before(getCreatedTime(token));
    }

}

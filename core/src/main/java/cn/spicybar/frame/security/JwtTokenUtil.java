package cn.spicybar.frame.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author hackyo
 * @version V1.0.0
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private static SecretKey secretKey = Keys.secretKeyFor(signatureAlgorithm);

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        Date expirationDate = new Date(System.currentTimeMillis() + 2592000L * 1000);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(secretKey, signatureAlgorithm).compact();
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaims(token);
            claims.put("created", new Date());
            Date expirationDate = new Date(System.currentTimeMillis() + 2592000L * 1000);
            refreshedToken = Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(secretKey, signatureAlgorithm).compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaims(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取创建日期
     *
     * @param token 令牌
     * @return 创建日期
     */
    private Date getCreatedTime(String token) {
        Date created;
        try {
            Claims claims = getClaims(token);
            created = new Date((Long) claims.get("created"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 验证令牌有效性
     *
     * @param token                 令牌
     * @param currentIp             用户当前登录IP
     * @param lastLoginIp           用户最后登录IP
     * @param lastPasswordResetTime 用户最后修改密码时间
     * @return 是否有效
     */
    public Boolean validateToken(String token, String currentIp, String lastLoginIp, Date lastPasswordResetTime) {
        return new Date().before(getClaims(token).getExpiration()) && currentIp.equals(lastLoginIp) && lastPasswordResetTime.before(getCreatedTime(token));
    }

}

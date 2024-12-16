package com.david.util;

import com.david.domain.model.OAuth2Provider;
import com.david.domain.model.OAuth2User;
import com.david.domain.model.Role;
import com.david.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JwtUtils {
    private static final String SECRET = "sdfsASD123qegvkflJBKJDCVBJKioseufjs23890FVDHJSK312ed/SDFu12";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes()); // 隨機生成密鑰
    private static final long EXPIRATION_TIME = 3600000; // 1 小時（以毫秒為單位）

    /**
     * 生成 JWT Token
     *
     * @param user
     * @param role
     * @return
     */
    public static String generateToken(User user, List<Role> role) {
        List<String> roleList = role.stream()
                .map(Role::getName)
                .toList();
        return Jwts.builder()
                .setSubject(user.getId().toString()) // Token 主題
                .claim("role", roleList) // 自定義聲明
                .claim("email", user.getEmail())
                .setIssuedAt(new Date()) // 簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 過期時間
                .signWith(SECRET_KEY) // 簽名密鑰
                .compact();
    }

    public static String generateToken (OAuth2User user) {
        // 預設給 Oauth2 登入者 ROLE_MEMBER 角色權限
        Claims claims = Jwts.claims();
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("role",List.of("ROLE_USER"))
                .claim("provider",user.getProvider())
                .claim("name",user.getName())
                .claim("userId",user.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }




    /**
     * 驗證 JWT Token
     *
     * @param token JWT Token
     * @return 用戶名（若驗證成功）
     */
    public static Optional<Map<String, Object>> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

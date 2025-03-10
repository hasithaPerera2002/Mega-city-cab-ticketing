package org.icbt.hasitha.megacity.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.icbt.hasitha.megacity.service.UserService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final UserService userService = new UserService();



    public static boolean isAdmin(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("isAdmin", Boolean.class);
    }

    public static String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 3600000); // 1 hour expiration

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        if(userService.isAdmin(username)){
            claims.put("isAdmin", true);
        }else {
            claims.put("isAdmin", false);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static String validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isTokenValid(String token) {
        return validateToken(token) != null;
    }

    public static String getUsernameFromToken(String token) {
        return validateToken(token);
    }

    private static boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public static boolean isTokenValidAndNotExpired(String token) {
        return isTokenValid(token) && !isTokenExpired(token);
    }
}

package org.icbt.hasitha.megacity.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.icbt.hasitha.megacity.service.UserService;
import org.icbt.hasitha.megacity.util.enums.RoleType;
import org.slf4j.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_STRING = "mega-cab-secret-k3134134141mega-cab-secret-k3134134141";
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private static final UserService userService = new UserService();
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JwtUtil.class);

    public static boolean isAdmin(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("isAdmin", Boolean.class);
    }

    public static String generateToken(String email, RoleType role) {
        LOGGER.info("Generating token for user: " + email);
        try {
            Date now = new Date();
            Date expireDate = new Date(now.getTime() + 3600000); // 1 hour expiration
            LOGGER.info("Generating token for user: " + email);
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            if (role == RoleType.ADMIN) {
                claims.put("isAdmin", true);
            } else {
                claims.put("isAdmin", false);
            }

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expireDate)
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        } catch (Exception e) {
            LOGGER.error("Error generating token: " + e.getMessage());
            return null;
        }
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

package org.icbt.hasitha.megacity.util;

import io.jsonwebtoken.Claims;
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
    private static final String SECRET_STRING = "3f4d5893d271917a4a5933d1b6f919decd2818c30e436c94a8a60d6b3c954674015382a750051f162b5a07b4b4d34bc894131884f870c88cfafe5fe58187c9f08897d6cbfeef7cd1f6770c40512fb44587decbb084ac54dcef1ebebb5890841a73d5f256460ebe399915a6fb008a5ea110a2c3f62f6fac7864fba216e57ccb495431ec104533ed496c79d8358863199f71c3a60e671a95e1277c02a9152d6c19279af13989b2fb9225984ec0ca16196e90cd0b4f341fe574b20d6a73d7827b0d907fb47b3fa965d1bfd80b0d4895ad15bb556691fc5354196b30a85038acf648f0f8882a86221007da946154070e3b1bed3cb38234f5ccae8cdc27af7de03a74";
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

    public static boolean isTokenExpired(String token) {
        // Use parserBuilder() to avoid deprecated methods
        LOGGER.info("Checking if token has expired");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Set signing key using SECRET_KEY
                .build()
                .parseClaimsJws(token)
                .getBody();

        boolean isExpired = claims.getExpiration().before(new Date());
        LOGGER.info(String.valueOf(isExpired));
        if (isExpired) {
            System.out.println("Token is expired");
        } else {
            System.out.println("Token is not expired");
        }

        return isExpired;

    }


}

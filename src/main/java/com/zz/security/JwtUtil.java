package com.zz.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "yourSecret";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    public static String validateToken(String token) {
        try {
            String userName = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
            System.out.println(userName);
            return userName;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

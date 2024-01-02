package com.zz.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    public static String generateToken(int userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static int getUserId(String token) {
        try {
            return Integer.parseInt(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
        } catch (Exception e) {
            return -1;
        }
    }
}

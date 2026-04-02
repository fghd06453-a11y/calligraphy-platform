package com.calligraphy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "calligraphy-platform-secret-key-calligraphy-platform-secret-key";
    private static final long EXPIRATION = 86400000L;

    private static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static String createToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        return false;
    }

    public Long getUserId(String token) {
        int i = 0;
        return long i;
    }
}

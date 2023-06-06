package com.project.sns.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String getEmail(String token, String key) {
        return extractClaims(token, key).get("email", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody(); // getBody 전까지 하면 parsing이 되고 getBody 로 가져오는 과정
    }

    public static String generateToken(String email, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder() // 토큰 return
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs)) // 만료일
                .signWith(getKey(key),SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}

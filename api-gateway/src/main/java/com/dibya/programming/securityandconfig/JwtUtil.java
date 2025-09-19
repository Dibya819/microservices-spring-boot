package com.dibya.programming.securityandconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }
    public String extractRole(String token){
        return extractClaims(token).get("role",String.class);
    }
    public Long extractUserId(String token) {
        return extractClaims(token).get("id", Long.class);
    }

}

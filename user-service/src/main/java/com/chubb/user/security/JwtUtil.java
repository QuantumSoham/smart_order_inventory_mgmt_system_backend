package com.chubb.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET =
		    "super-secret-key-at-least-32-bytes-long";
    private static final long EXPIRY =
        1000 * 60 * 60; // 1 hour

    private final Key key =
        Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Long userId, String email,String role) {
        return Jwts.builder()
                .setSubject("auth")
                .claim("userId", userId)
                .claim("email",email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRY)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

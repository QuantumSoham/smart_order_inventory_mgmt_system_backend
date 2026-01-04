package com.chubb.gateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Component
public class JwtUtil {

	private static final String SECRET =
		    "super-secret-key-at-least-32-bytes-long";
    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public boolean isTokenValid(String token) {
        try {
            validate(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return validate(token)
                .get("userId", Long.class);
    }

//    @SuppressWarnings("unchecked")
//    public List<String> getRoles(String token) {
//        return validate(token)
//                .get("roles", List.class);
//    }
    
  public String getRole(String token) {
  return validate(token)
          .get("role", String.class);
}
    private Claims validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

package com.microservice.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private  static  final String SECRET="mySecretKeyThatIKINGMALLIughAndSecure123456789";



    public String generateToken(UUID userId, String role, String email){
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(new Date())
                // 1 hour expiration
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Claims validateToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}

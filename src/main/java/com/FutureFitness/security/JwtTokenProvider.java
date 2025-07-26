package com.FutureFitness.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component // Adding @Component annotation so Spring can detect and autowire this bean
public class JwtTokenProvider {

    // Hard-code a default value to avoid property placeholder resolution
    private String jwtSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private long jwtExpirationDate = 86400000; // 24 hours

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    // Generate token for Staff based on email
    public String generateTokenForStaff(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Extract username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT claims string is empty");
        }
    }
}

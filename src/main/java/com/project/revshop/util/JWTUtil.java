package com.project.revshop.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {
    // Base64-encoded 512-bit (64-byte) static key
    private final String SECRET_KEY_BASE64 = "SoE7+u/5gnnvN4GWu+N+gevErJY75l3by5Y21bqsM5DsJ1WObramptITv0vhr2lVvTWLJ+F9Ci6d05r8b773jw==";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_BASE64));
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String generateToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token, String userEmail) {
        String subject = extractUserEmail(token);
        return (subject.equals(userEmail) && !isTokenExpired(token));
    }

    public String extractUserEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getExpiration();
    }
}

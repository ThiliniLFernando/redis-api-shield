package dev.thilinifernando.gatekeeper.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${gateway.jwt.secret}")
    private String secret;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .requireIssuer("api-gateway")   // pin issuer
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String issue(String userId, String plan) {
        return Jwts.builder()
                .subject(userId)
                .claim("plan", plan)
                .issuer("api-gateway")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(signingKey())          // defaults to HS256
                .compact();
    }

    @PostConstruct
    public void validate() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "FATAL: gateway.jwt.secret is not set. " +
                            "Generate one with: openssl rand -base64 32"
            );
        }

        // Also validate the key is strong enough — 32 bytes minimum for HS256
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "FATAL: JWT secret is too short. Minimum 32 bytes (256 bits) required."
            );
        }
    }
}
package fh.dualo.kidsapp.application.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;


public class JwtUtil
{

    private final SecretKey secretKey;
    private final JwtParser parser;

    public JwtUtil() {
        this.secretKey = Jwts.SIG.HS256.key().build();
        this.parser = Jwts.parser().verifyWith(secretKey).build();
    }

    /**
     * Generates a JWT with the given claims and expiration in seconds.
     */
    public String generateToken(Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the JWT signature and expiration.
     */
    public boolean isTokenValid(String token) {
        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extracts claims from a valid token.
     */
    public Map<String, Object> getClaims(String token) {
        if (isTokenValid(token)) {
            return parser.parseSignedClaims(token).getPayload();
        }
        return Map.of();
    }
}

package com.leman.contentmanagementapi.security;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType.ACCESS;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType.REFRESH;

import com.leman.contentmanagementapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String USERNAME_CLAIM = "username";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH, refreshTokenExpiration);
    }

    private String generateToken(User user, String type, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim(USERNAME_CLAIM, user.getUsername())
                .claim(TOKEN_TYPE_CLAIM, type)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }


    public Long getUserIdFromToken(String token) {
        String subject = extractAllClaims(token).getSubject();
        return Long.valueOf(subject);
    }

    public String getTokenType(String token) {
        return extractAllClaims(token).get(TOKEN_TYPE_CLAIM, String.class);
    }

    public boolean validateToken(String token, Long userId) {
        try {
            Claims claims = extractAllClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            return Long.valueOf(claims.getSubject()).equals(userId);
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

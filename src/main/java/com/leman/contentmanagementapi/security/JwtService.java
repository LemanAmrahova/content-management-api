package com.leman.contentmanagementapi.security;

import com.leman.contentmanagementapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType.ACCESS;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType.REFRESH;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
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

    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).get(USERNAME_CLAIM, String.class);
    }

    public String getTokenType(String token) {
        return extractAllClaims(token).get(TOKEN_TYPE_CLAIM, String.class);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, Long userId) {
        if (!validateToken(token)) {
            return false;
        }
        Long tokenUserId = getUserIdFromToken(token);
        return tokenUserId.equals(userId);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

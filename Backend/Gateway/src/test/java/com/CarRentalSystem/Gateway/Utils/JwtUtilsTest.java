package com.CarRentalSystem.Gateway.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String SECRET = "this-is-a-test-secret-key-32chars!!";

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecretKey", SECRET);
    }

    private String buildToken(String subject, String userId, long expiryMs) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(key)
                .compact();
    }

    @Test
    @DisplayName("extractClaims – returns correct subject from valid token")
    void extractClaims_validToken_subject() {
        String token = buildToken("alice", "user-001", 60_000);

        Claims claims = jwtUtils.extractClaims(token);

        assertThat(claims.getSubject()).isEqualTo("alice");
    }

    @Test
    @DisplayName("extractClaims – returns correct userId claim")
    void extractClaims_validToken_userId() {
        String token = buildToken("alice", "user-001", 60_000);

        assertThat(jwtUtils.extractClaims(token).get("userId")).isEqualTo("user-001");
    }

    @Test
    @DisplayName("extractClaims – throws ExpiredJwtException for expired token")
    void extractClaims_expiredToken_throws() {
        String expired = buildToken("alice", "user-001", -1_000);

        assertThatThrownBy(() -> jwtUtils.extractClaims(expired))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("extractClaims – throws for tampered token")
    void extractClaims_tamperedToken_throws() {
        String token = buildToken("alice", "user-001", 60_000) + "tampered";

        assertThatThrownBy(() -> jwtUtils.extractClaims(token))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("getSecretKey – returns HmacSHA256 key")
    void getSecretKey_returnsValidKey() {
        SecretKey key = jwtUtils.getSecretKey();

        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isEqualTo("HmacSHA256");
    }
}
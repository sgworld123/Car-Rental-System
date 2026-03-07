package com.CarRentalSystem.Gateway.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Optional<Claims> validateAndExtractClaims(String token)
    {
        try
        {
            return Optional.of(extractClaims(token));
        }
        catch(ExpiredJwtException e)
        {
            log.warn("JWT token expired: {}", e.getMessage());
        }
        catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        }
        catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
        }
        return Optional.empty();
    }

    public SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

}

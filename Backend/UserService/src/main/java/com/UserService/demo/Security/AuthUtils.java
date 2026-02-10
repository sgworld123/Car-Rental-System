package com.UserService.demo.Security;

import com.UserService.demo.Model.AuthUser;
import com.UserService.demo.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;


@Component
public class AuthUtils {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey ;

    public SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(AuthUser authUser)
    {
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("userId",authUser.getId());
        claims.put("roles",authUser.getAuthorities()
                .stream()
                .map(a-> a.getAuthority())
                .toList());

        return Jwts.builder()
                .setSubject(authUser.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}

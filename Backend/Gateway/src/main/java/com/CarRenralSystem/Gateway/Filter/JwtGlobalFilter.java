package com.CarRenralSystem.Gateway.Filter;

import com.CarRenralSystem.Gateway.Utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(path.contains("/api/auth/login") || path.contains("/api/auth/signup"))
        {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(authHeader == null || !authHeader.contains("Bearer"))
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.split("Bearer")[1].trim();

        Claims claims = JwtUtils.extractClaims(token);

        String username = claims.getSubject();
        String userId = claims.get("userId").toString();

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-Username", username)
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}

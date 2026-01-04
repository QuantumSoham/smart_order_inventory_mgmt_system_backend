package com.chubb.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter
        implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public int getOrder() {
        // 🔑 Run AFTER route is resolved
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getURI().getPath();

        // Allow preflight
        if (method == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        var rolesOpt = RbacRules.resolve(method, path);

        // Endpoint not secured → let routing proceed untouched
        if (rolesOpt.isEmpty()) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return deny(exchange, "Missing JWT");
        }

        Claims claims;
        try {
            claims = jwtUtil.validate(authHeader.substring(7));
        } catch (Exception e) {
            return deny(exchange, "Invalid JWT");
        }

        String role = claims.get("role", String.class);

        if (!rolesOpt.get().contains(role)) {
            return deny(exchange, "Access denied");
        }

        // ✅ Authorized → pass through without altering routing
        return chain.filter(exchange);
    }

    private Mono<Void> deny(
            ServerWebExchange exchange,
            String msg
    ) {
        exchange.getResponse()
                .setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, "application/json");

        byte[] body =
                ("{\"error\":\"" + msg + "\"}").getBytes();

        return exchange.getResponse()
                .writeWith(Mono.just(
                        exchange.getResponse()
                                .bufferFactory()
                                .wrap(body)
                ));
    }
}

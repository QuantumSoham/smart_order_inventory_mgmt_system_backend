package com.chubb.gateway.security;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import java.util.*;

public final class RbacRules {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<Rule> RULES = new ArrayList<>();

    static {
        // 🔐 SECURE ONE ENDPOINT FOR NOW
        allow(HttpMethod.GET, "/warehouses/**", "USER");
    }

    private static void allow(
            HttpMethod method,
            String pathPattern,
            String... roles
    ) {
        RULES.add(new Rule(method, pathPattern, Set.of(roles)));
    }

    public static Optional<Set<String>> resolve(
            HttpMethod method,
            String requestPath
    ) {
        return RULES.stream()
                .filter(r ->
                        r.method == method &&
                        matcher.match(r.pathPattern, requestPath)
                )
                .map(r -> r.roles)
                .findFirst();
    }

    private record Rule(
            HttpMethod method,
            String pathPattern,
            Set<String> roles
    ) {}
}

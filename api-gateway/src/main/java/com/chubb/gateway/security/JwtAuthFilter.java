package com.chubb.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

	private final JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		System.out.println("ALL HEADERS = " + exchange.getRequest().getHeaders());

		String path = exchange.getRequest().getURI().getPath();
		HttpMethod method = exchange.getRequest().getMethod();


		// ✅ Allow CORS preflight
		if (method == HttpMethod.OPTIONS) {
			return chain.filter(exchange);
		}

		// ✅ Public endpoints
		if (isPublicEndpoint(path, method)) {
			return chain.filter(exchange);
		}

		// 🔐 Read Authorization header
		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return unauthorized(exchange);
		}

		String token = authHeader.substring(7);
		System.out.println("PATH=" + path + " TOKEN=" + token);


		if (!jwtUtil.isTokenValid(token)) {
			System.out.println("Invalid Token ___________________________");
			return unauthorized(exchange);
		}

		Long userId = jwtUtil.getUserId(token);
		String role = jwtUtil.getRole(token);
		System.out.println("ROLE"+role);

		// 🔎 Role-based authorization
		if (!isAuthorized(path, method, role)) {
			return forbidden(exchange);
		}

		// ➕ Forward user context
		ServerWebExchange mutated = exchange.mutate()
				.request(r -> r.header("X-USER-ID", String.valueOf(userId)).header("X-USER-ROLES", role)).build();

		return chain.filter(mutated);
	}

	//public endpoints
	private boolean isPublicEndpoint(String path, HttpMethod method) {

		if(path.equals("/ai/billing") && method==HttpMethod.POST)
		{
			return true;
		}
		if (path.startsWith("/auth/")) {
			return true;
		}

		if (path.startsWith("/products") && method == HttpMethod.GET) {
			return true;
		}
		if (path.startsWith("/warehouses") && method == HttpMethod.GET) {
			return true;
		}
		if ((path.startsWith("/users/login") || path.startsWith("/users/register")) && method == HttpMethod.POST) {
			return true;
		}
		return false;
	}

	// =========================
	// AUTHORIZATION RULES
	// =========================
	private boolean isAuthorized(String path, HttpMethod method, String role) {

		boolean isAdmin = role.equals("ADMIN") || role.equals("ROLE_ADMIN");
		boolean isUser = role.equals("USER") || role.equals("ROLE_USER");
		boolean isSales = role.equals("SALES_EXEC") || role.equals("ROLE_SALES_EXEC");
		boolean isWarehouse = role.equals("WAREHOUSE_MANAGER") || role.equals("ROLE_WAREHOUSE_MANAGER");
		boolean isFinance = role.equals("FINANCE") || role.equals("ROLE_FINANCE");

		System.out.println("ROLES = " + role + " PATH = " + path);

// ADMIN ONLY
		if (path.startsWith("/admin/")) {
			return isAdmin;
		}

		if (path.equals("/users") || path.startsWith("/users/")) {
			return isAdmin;
		}

		if (path.startsWith("/products/") || (path.equals("/products") && method == HttpMethod.POST)
				&& (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE)) {
			return isAdmin || isSales || isWarehouse;
		}
		
		if(path.matches("/orders/.+/cancel") && method==HttpMethod.PUT)
		{
			return isAdmin || isUser;
		}

// ORDERS
		if (path.equals("/orders") && method == HttpMethod.POST) {
			return isUser || isSales || isAdmin;
		}

		if (path.startsWith("/orders/") && method == HttpMethod.GET) {
			return isUser || isSales || isAdmin;
		}
		if (path.startsWith("/admin/analytics/") && method == HttpMethod.GET) {
			return  isSales || isAdmin;
		}

		if (path.matches("^/orders/.+/status$") && method == HttpMethod.PUT) {
			return isWarehouse || isAdmin;
		}

// INVENTORY
		if (path.matches("^/inventory/warehouse/.*$") || path.startsWith("/inventory/") || path.equals("/inventory") || path.startsWith("/fulfillment/") || path.startsWith("/warehouses/")) {
			return isWarehouse || isUser || isSales || isAdmin;
		}

// BILLING
		if (path.startsWith("/billing/") || path.startsWith("/reports/") || path.equals("/warehouses")) {
			return isFinance || isAdmin || isUser;
		}

		return false;
	}

//    private boolean isAuthorized(String path,
//                                 HttpMethod method,
//                                 List<String> roles) {
//
//        boolean isAdmin = roles.contains("ADMIN");
//        boolean isUser = roles.contains("USER");
//        boolean isSales = roles.contains("SALES_EXEC");
//        boolean isWarehouse = roles.contains("WAREHOUSE_MANAGER");
//        boolean isFinance = roles.contains("FINANCE");
//        System.out.println(roles);
//        // -------- ADMIN ONLY --------
//        if (path.startsWith("/admin/")) {
//            return isAdmin;
//        }
//
//        if (path.startsWith("/users") || path.startsWith("^/users/.+/deactivate$")) 
//        {
//            return isAdmin;
//        }
//
//        if (path.startsWith("/products/")
//                && (method == HttpMethod.POST
//                || method == HttpMethod.PUT
//                || method == HttpMethod.DELETE)) {
//            return isAdmin;
//        }
//
//        // -------- ORDERS --------
//        if (path.equals("/orders")
//                && method == HttpMethod.POST) {
//            return isUser || isSales || isAdmin;
//        }
//
//        if (path.startsWith("/orders/")
//                && method == HttpMethod.GET) {
//            return isUser || isSales || isAdmin;
//        }
//
//        if (path.matches("^/orders/.+/status$")
//                && method == HttpMethod.PUT) {
//            return isWarehouse || isAdmin;
//        }
//
//        // -------- INVENTORY / WAREHOUSE --------
//        if (path.startsWith("/inventory/")
//                || path.startsWith("/fulfillment/")
//                || path.startsWith("/warehouses/")) {
//            return isWarehouse || isAdmin;
//        }
//
//        // -------- BILLING / REPORTS --------
//        if (path.startsWith("/billing/")
//                || path.startsWith("/reports/")) {
//            return isFinance || isAdmin;
//        }
//
//        // Default deny
//        return false;
//    }

	private Mono<Void> unauthorized(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

	private Mono<Void> forbidden(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		return exchange.getResponse().setComplete();
	}

	@Override
	public int getOrder() {
		// Run early, before routing
		return -1;
	}
}
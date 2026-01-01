package com.chubb.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.chubb.order.dto.request.CreateOrderRequest;
import com.chubb.order.dto.response.OrderResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderUserController {

    private final OrderService service;

    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest req) {
        return service.create(req);
    }

    @GetMapping("/user/{userId}")
    public List<OrderSummaryResponse> getUserOrders(
            @PathVariable Long userId) {
        return service.getUserOrders(userId);
    }

    @PutMapping("/{orderId}/cancel")
    public void cancel(
            @PathVariable Long orderId,
            @RequestHeader("X-USER-ID") Long userId) {
        service.cancel(orderId, userId);
    }
}

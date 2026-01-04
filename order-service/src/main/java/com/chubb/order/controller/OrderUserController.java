package com.chubb.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chubb.order.dto.request.CreateOrderRequest;
import com.chubb.order.dto.response.OrderDetailedResponse;
import com.chubb.order.dto.response.OrderResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.service.OrderService;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderUserController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest req) {
        OrderResponse response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDetailedResponse>> getUserOrders(@PathVariable Long userId) {
        List<OrderDetailedResponse> orders = service.getUserOrders(userId);
        return ResponseEntity.ok(orders); 
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderSummaryResponse> getUserOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getUserOrderStatus(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long orderId,
            @RequestHeader("X-USER-ID") Long userId) {
        service.cancel(orderId, userId);
        return ResponseEntity.noContent().build();
    }
}
//@RestController
//@RequestMapping("/orders")
//@RequiredArgsConstructor
//public class OrderUserController {
//
//    private final OrderService service;
//
//    @PostMapping
//    public OrderResponse create(@RequestBody CreateOrderRequest req) {
//        return service.create(req);
//    }
//
//    @GetMapping("/user/{userId}")
//    public List<OrderSummaryResponse> getUserOrders(
//            @PathVariable Long userId) {
//        return service.getUserOrders(userId);
//    }
//    @GetMapping("/{orderId}/status")
//    public OrderSummaryResponse getUserOrderStatus(
//            @PathVariable Long orderId) {
//        return service.getUserOrderStatus(orderId);
//    }
//    @PutMapping("/{orderId}/cancel")
//    public void cancel(
//            @PathVariable Long orderId,
//            @RequestHeader("X-USER-ID") Long userId) {
//        service.cancel(orderId, userId);
//    }
//}

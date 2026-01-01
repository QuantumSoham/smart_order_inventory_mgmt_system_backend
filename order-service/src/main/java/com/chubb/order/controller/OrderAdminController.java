package com.chubb.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;


import com.chubb.order.dto.response.OrderStatusResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService service;

    @GetMapping("/status/{status}")
    public List<OrderSummaryResponse> byStatus(
            @PathVariable OrderStatus status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{orderId}/status/{status}")
    public OrderStatusResponse updateStatus(
            @PathVariable Long orderId,
            @PathVariable OrderStatus status) {
        return service.updateStatus(orderId, status);
    }
}

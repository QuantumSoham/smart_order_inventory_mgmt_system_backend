package com.chubb.order.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.chubb.order.dto.analytics.*;
import com.chubb.order.service.OrderAnalyticsService;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
public class OrderAnalyticsController {

    private final OrderAnalyticsService service;

    @GetMapping("/kpis")
    public OrderKpiResponse kpis() {
        return service.getKpis();
    }

    @GetMapping("/orders-by-status")
    public List<OrderStatusCountDto> byStatus() {
        return service.ordersByStatus();
    }

    @GetMapping("/orders-by-warehouse")
    public List<WarehouseOrderCountDto> byWarehouse() {
        return service.ordersByWarehouse();
    }

    @GetMapping("/revenue-by-warehouse")
    public List<WarehouseRevenueDto> revenueByWarehouse() {
        return service.revenueByWarehouse();
    }

    @GetMapping("/revenue-by-date")
    public List<RevenueByDateDto> revenueByDate(
        @RequestParam LocalDate from,
        @RequestParam LocalDate to
    ) {
        return service.revenueByDate(from, to);
    }

    @GetMapping("/orders-by-state")
    public List<OrdersByStateDto> byState() {
        return service.ordersByState();
    }
}

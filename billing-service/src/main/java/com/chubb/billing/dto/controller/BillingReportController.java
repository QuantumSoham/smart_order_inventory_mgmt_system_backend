package com.chubb.billing.dto.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.chubb.billing.dto.response.*;

import com.chubb.billing.service.BillingReportService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing/reports")
@RequiredArgsConstructor
public class BillingReportController {

    private final BillingReportService reportService;

    @GetMapping("/revenue")
    public List<RevenueReportResponse> revenueByDate(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        return reportService.revenueByDate(from, to);
    }

    @GetMapping("/sales/category")
    public List<CategorySalesResponse> byCategory() {
        return reportService.salesByCategory();
    }

    @GetMapping("/sales/warehouse")
    public List<WarehouseSalesResponse> byWarehouse() {
        return reportService.salesByWarehouse();
    }

    @GetMapping("/top-products")
    public List<TopProductResponse> topProducts() {
        return reportService.topProducts();
    }
}

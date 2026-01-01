package com.chubb.billing.service;

import org.springframework.stereotype.Service;

import com.chubb.billing.dto.response.CategorySalesResponse;
import com.chubb.billing.dto.response.RevenueReportResponse;
import com.chubb.billing.dto.response.TopProductResponse;
import com.chubb.billing.dto.response.WarehouseSalesResponse;
import com.chubb.billing.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingReportService {

    private final InvoiceRepository invoiceRepo;

    public List<RevenueReportResponse> revenueByDate(
            LocalDate from,
            LocalDate to) {

        return invoiceRepo.revenueByDate(
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        ).stream()
         .map(r -> new RevenueReportResponse(
                 r[0].toString(),
                 (BigDecimal) r[1]
         ))
         .toList();
    }

    public List<CategorySalesResponse> salesByCategory() {
        return invoiceRepo.salesByCategory().stream()
                .map(r -> new CategorySalesResponse(
                        (String) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    public List<WarehouseSalesResponse> salesByWarehouse() {
        return invoiceRepo.salesByWarehouse().stream()
                .map(r -> new WarehouseSalesResponse(
                        (Long) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    public List<TopProductResponse> topProducts() {
        return invoiceRepo.topProducts().stream()
                .map(r -> new TopProductResponse(
                        (String) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }
}


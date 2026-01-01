package com.chubb.billing.dto.controller;

import org.springframework.web.bind.annotation.*;

import com.chubb.billing.dto.request.CreateInvoiceRequest;
import com.chubb.billing.dto.response.InvoiceResponse;
import com.chubb.billing.dto.response.PaymentResponse;
import com.chubb.billing.service.BillingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/billing/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final BillingService service;

    @PostMapping
    public InvoiceResponse create(@RequestBody CreateInvoiceRequest req) {
        return service.createInvoice(req);
    }

    @GetMapping("/{orderId}")
    public InvoiceResponse get(@PathVariable Long orderId) {
        return service.getInvoice(orderId);
    }

    @PostMapping("/{invoiceId}/pay")
    public PaymentResponse pay(@PathVariable Long invoiceId) {
        return service.pay(invoiceId);
    }
}

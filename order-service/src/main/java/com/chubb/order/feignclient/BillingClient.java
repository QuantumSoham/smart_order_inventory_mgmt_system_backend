package com.chubb.order.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.chubb.order.dto.request.CreateInvoiceRequest;
import com.chubb.order.dto.response.InvoiceResponse;

@FeignClient(name = "billing-service")
public interface BillingClient {

    @PostMapping("/billing/invoices")
    InvoiceResponse createInvoice(
            @RequestBody CreateInvoiceRequest request
    );
}

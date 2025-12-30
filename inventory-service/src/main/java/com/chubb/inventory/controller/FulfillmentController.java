package com.chubb.inventory.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;
import com.chubb.inventory.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fulfillment")
@RequiredArgsConstructor
public class FulfillmentController {

    private final FulfillmentService service;

    @PostMapping("/assign")
    public StockActionResponse assign(
            @RequestBody AssignFulfillmentRequest req) {

        service.assign(req);
        return new StockActionResponse("PENDING", req.getOrderId());
    }

    @PutMapping("/{orderId}/status")
    public StockActionResponse update(
            @PathVariable Long orderId,
            @RequestBody UpdateFulfillmentStatusRequest req) {

        service.updateStatus(orderId, req.getStatus());
        return new StockActionResponse(req.getStatus(), orderId);
    }

    @GetMapping("/{orderId}")
    public FulfillmentResponse get(@PathVariable Long orderId) {
        return service.get(orderId);
    }
}

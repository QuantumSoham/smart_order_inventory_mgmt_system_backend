package com.chubb.order.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "billing-service")
public interface BillingClient {

    @PostMapping("/billing/init/{orderId}")
    Map<String, Object> init(@PathVariable Long orderId);
}

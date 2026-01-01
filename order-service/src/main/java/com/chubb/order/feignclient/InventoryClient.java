package com.chubb.order.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.chubb.order.dto.response.ProductResponse;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/products/{productId}")
    ProductResponse getProduct(@PathVariable Long productId);

    @PostMapping("/inventory/reserve")
    void reserve(@RequestBody Map<String, Object> body);

    @PostMapping("/inventory/release")
    void release(@RequestBody Map<String, Long> body);

    @PostMapping("/inventory/commit")
    void commit(@RequestBody Map<String, Long> body);
}

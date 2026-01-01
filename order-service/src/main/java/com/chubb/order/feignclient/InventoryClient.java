package com.chubb.order.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/products/{productId}")
    Map<String, Object> getProduct(@PathVariable Long productId);

    @PostMapping("/inventory/reserve")
    void reserve(@RequestBody Map<String, Object> body);

    @PostMapping("/inventory/release")
    void release(@RequestBody Map<String, Long> body);

    @PostMapping("/inventory/commit")
    void commit(@RequestBody Map<String, Long> body);
}

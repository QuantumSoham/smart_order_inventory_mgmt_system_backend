package com.chubb.inventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;
import com.chubb.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping
    public AddInventoryResponse add(@RequestBody AddInventoryRequest req) {
        return service.addStock(req);
    }

    @PutMapping("/{id}")
    public MessageResponse update(@PathVariable Long id,
                                  @RequestBody UpdateInventoryRequest req) {
        service.updateStock(id, req);
        return new MessageResponse("Stock updated");
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<InventoryByWarehouseResponse> byWarehouse(
            @PathVariable Long warehouseId) {
        return service.getByWarehouse(warehouseId);
    }

    @GetMapping("/product/{productId}")
    public List<InventoryByProductResponse> byProduct(
            @PathVariable Long productId) {
        return service.getByProduct(productId);
    }

    @GetMapping("/category/{category}")
    public List<InventoryByCategoryResponse> getByCategory(
            @PathVariable String category) {

        return service.getByCategory(category);
    }

    @GetMapping("/low-stock")
    public List<LowStockResponse> lowStock() {
        return service.getLowStock();
    }

    @PostMapping("/reserve")
    public StockActionResponse reserve(@RequestBody ReserveStockRequest req) {
        service.reserve(req);
        return new StockActionResponse("RESERVED", req.getOrderId());
    }

    @PostMapping("/release")
    public StockActionResponse release(@RequestBody OrderActionRequest req) {
        service.release(req.getOrderId());
        return new StockActionResponse("RELEASED", req.getOrderId());
    }

    @PostMapping("/commit")
    public StockActionResponse commit(@RequestBody OrderActionRequest req) {
        service.commit(req.getOrderId());
        return new StockActionResponse("COMMITTED", req.getOrderId());
    }
    
}

package com.chubb.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.chubb.inventory.entity.*;
import com.chubb.inventory.service.*;
import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    public WarehouseResponse create(@RequestBody WarehouseRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<WarehouseResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public WarehouseResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public MessageResponse update(@PathVariable Long id,
                                  @RequestBody WarehouseRequest req) {
        service.update(id, req);
        return new MessageResponse("Warehouse updated");
    }

    @DeleteMapping("/{id}")
    public MessageResponse disable(@PathVariable Long id) {
        service.disable(id);
        return new MessageResponse("Warehouse disabled");
    }
}

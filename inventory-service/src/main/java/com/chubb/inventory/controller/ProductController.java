package com.chubb.inventory.controller;

import com.chubb.inventory.dto.request.CreateProductRequest;
import com.chubb.inventory.dto.response.MessageResponse;
import com.chubb.inventory.dto.response.ProductResponse;
import com.chubb.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // CREATE
    @PostMapping
    public ProductResponse create(@RequestBody CreateProductRequest req) {
        return service.create(req);
    }

    // READ ALL (active only)
    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody CreateProductRequest req) {
        return service.update(id, req);
    }

    // DELETE (SOFT DELETE)
    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        service.delete(id);
        return new MessageResponse("Product disabled successfully");
    }
}

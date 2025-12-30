package com.chubb.inventory.controller;

import com.chubb.inventory.dto.request.CreateProductRequest;
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

    @PostMapping
    public ProductResponse create(@RequestBody CreateProductRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAll();
    }
}

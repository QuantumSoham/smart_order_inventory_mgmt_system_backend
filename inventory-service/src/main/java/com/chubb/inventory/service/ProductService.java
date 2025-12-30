package com.chubb.inventory.service;

import com.chubb.inventory.dto.request.CreateProductRequest;
import com.chubb.inventory.dto.response.ProductResponse;
import com.chubb.inventory.entity.Product;
import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;

    public ProductResponse create(CreateProductRequest req) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(req.getCategory());
        p.setImageUrl(req.getImageUrl());
        p.setActive(true);

        return toResponse(productRepo.save(p));
    }

    public Product getEntity(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() ->
                        new BusinessException("Unknown product. Add product first."));
    }

    public List<ProductResponse> getAll() {
        return productRepo.findAll().stream()
                .filter(Product::isActive)
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getCategory(),
                p.getImageUrl()
        );
    }
}

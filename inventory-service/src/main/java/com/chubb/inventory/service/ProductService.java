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

    // CREATE
    public ProductResponse create(CreateProductRequest req) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(req.getCategory());
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());
        p.setActive(true);

        return toResponse(productRepo.save(p));
    }

    // READ ALL (active only)
    public List<ProductResponse> getAll() {
        return productRepo.findAll().stream()
                .filter(Product::isActive)
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    public ProductResponse getById(Long id) {
        Product p = findActiveProduct(id);
        return toResponse(p);
    }

    // UPDATE
    public ProductResponse update(Long id, CreateProductRequest req) {
        Product p = findActiveProduct(id);

        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(req.getCategory());
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());

        return toResponse(productRepo.save(p));
    }

    // DELETE (SOFT DELETE)
    public void delete(Long id) {
        Product p = findActiveProduct(id);
        p.setActive(false);
        productRepo.save(p);
    }

    // INTERNAL USE (Inventory validation)
    public Product getEntity(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() ->
                        new BusinessException("Unknown product. Add product first."));
    }

    // PRIVATE HELPER
    private Product findActiveProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        if (!p.isActive())
            throw new BusinessException("Product is inactive");

        return p;
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getImageUrl(),
                p.getDescription(),
                p.getCategory(),
                p.getPrice()
        );
    }
}

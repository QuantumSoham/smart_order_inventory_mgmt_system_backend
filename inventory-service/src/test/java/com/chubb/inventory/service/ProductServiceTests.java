package com.chubb.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.inventory.dto.request.CreateProductRequest;
import com.chubb.inventory.dto.response.ProductResponse;
import com.chubb.inventory.entity.Product;
import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_and_getAll_filtersActive() {
        CreateProductRequest req = new CreateProductRequest();
        req.setName("P");
        req.setDescription("D");
        req.setCategory("C");
        req.setPrice(BigDecimal.valueOf(9.99));
        req.setImageUrl("img");

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("P");
        saved.setDescription("D");
        saved.setCategory("C");
        saved.setPrice(BigDecimal.valueOf(9.99));
        saved.setImageUrl("img");
        saved.setActive(true);

        when(productRepo.save(any(Product.class))).thenReturn(saved);
        when(productRepo.findAll()).thenReturn(List.of(saved));

        ProductResponse res = productService.create(req);
        assertEquals(1L, res.getId());

        List<ProductResponse> all = productService.getAll();
        assertEquals(1, all.size());
    }

    @Test
    void getById_notFound_throws() {
        when(productRepo.findById(5L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.getById(5L));
    }

    @Test
    void getEntity_unknownProduct_throws() {
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.getEntity(99L));
    }

    @Test
    void getById_success_and_findActive_inactive_throws() {
        Product p = new Product();
        p.setId(11L);
        p.setName("X");
        p.setActive(true);

        when(productRepo.findById(11L)).thenReturn(Optional.of(p));
        var resp = productService.getById(11L);
        assertEquals(11L, resp.getId());

        Product p2 = new Product();
        p2.setId(12L);
        p2.setActive(false);
        when(productRepo.findById(12L)).thenReturn(Optional.of(p2));

        BusinessException ex = assertThrows(BusinessException.class, () -> productService.getById(12L));
        assertTrue(ex.getMessage().contains("Product is inactive") || ex.getMessage().contains("Product not found"));
    }

    @Test
    void update_and_delete_behaviour() {
        Product existing = new Product();
        existing.setId(21L);
        existing.setName("A");
        existing.setActive(true);

        when(productRepo.findById(21L)).thenReturn(Optional.of(existing));

        CreateProductRequest upd = new CreateProductRequest();
        upd.setName("B");
        upd.setDescription("desc");
        upd.setCategory("cat");
        upd.setPrice(BigDecimal.valueOf(3.14));
        upd.setImageUrl("uimg");

        when(productRepo.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = productService.update(21L, upd);
        assertEquals("B", out.getName());

        productService.delete(21L);
        assertFalse(existing.isActive());
        verify(productRepo, times(2)).save(any(Product.class));
    }
}

package com.chubb.inventory.repository;



import com.chubb.inventory.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Used in reserve / release / commit
    Optional<Inventory> findByProductIdAndWarehouseId(
            Long productId,
            Long warehouseId
    );

    // GET /inventory/warehouse/{warehouseId}
    List<Inventory> findByWarehouseId(Long warehouseId);

    // GET /inventory/product/{productId}
    List<Inventory> findByProductId(Long productId);

    // Used for low-stock detection (optional optimization)
    List<Inventory> findByAvailableQuantityLessThanEqual(
            int threshold
    );
    
    List<Inventory> findByCategory(String category);
}

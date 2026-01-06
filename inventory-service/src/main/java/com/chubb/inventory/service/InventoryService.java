package com.chubb.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;
import com.chubb.inventory.entity.*;
import com.chubb.inventory.exception.*;
import com.chubb.inventory.repository.*;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepo;
    private final WarehouseRepository warehouseRepo;
    private final StockReservationRepository reservationRepo;
    private final ProductService productService;

    
//    public AddInventoryResponse addStock(AddInventoryRequest req) {
//        Warehouse wh = warehouseRepo.findById(req.getWarehouseId())
//                .orElseThrow(() -> new BusinessException("Warehouse not found"));
//
//        Inventory inv = new Inventory();
//        inv.setProductId(req.getProductId());
//        inv.setWarehouse(wh);
//        inv.setCategory(req.getCategory());
//        inv.setTotalQuantity(req.getQuantity());
//        inv.setAvailableQuantity(req.getQuantity());
//        inv.setReservedQuantity(0);
//        inv.setLowStockThreshold(req.getLowStockThreshold());
//
//        Inventory saved = inventoryRepo.save(inv);
//
//        return new AddInventoryResponse(saved.getId(), saved.getAvailableQuantity());
//    }

    public AddInventoryResponse addStock(AddInventoryRequest req) {

        // ✅ Validate product existence
        Product product = productService.getEntity(req.getProductId());

        Warehouse wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new BusinessException("Warehouse not found"));

        // 🚨 IMPORTANT CHECK
        inventoryRepo
            .findByProductIdAndWarehouseId(
                product.getId(),
                wh.getId()
            )
            .ifPresent(inv -> {
                throw new BusinessException(
                    "Inventory already exists for product "
                    + product.getName()
                    + " in warehouse "
                    + wh.getName()
                    + ". Use update stock instead."
                );
            });

        // ✅ Safe to create new inventory
        Inventory inv = new Inventory();
        inv.setProductId(product.getId());
        inv.setCategory(product.getCategory());
        inv.setWarehouse(wh);
        inv.setTotalQuantity(req.getQuantity());
        inv.setAvailableQuantity(req.getQuantity());
        inv.setReservedQuantity(0);
        inv.setLowStockThreshold(req.getLowStockThreshold());

        Inventory saved = inventoryRepo.save(inv);

        return new AddInventoryResponse(
                saved.getId(),
                saved.getAvailableQuantity()
        );
    }


    
    public void updateStock(Long id, UpdateInventoryRequest req) {
        Inventory inv = findInventory(id);
        inv.setCategory(req.getCategory());
        inv.setTotalQuantity(req.getQuantity());
        inv.setAvailableQuantity(req.getQuantity() - inv.getReservedQuantity());
    }

    public List<InventoryByWarehouseResponse> getByWarehouse(Long warehouseId) {
        return inventoryRepo.findByWarehouseId(warehouseId).stream()
                .map(i -> new InventoryByWarehouseResponse(
                		i.getId(),
                        i.getProductId(),
                        i.getCategory(),
                        i.getAvailableQuantity(),
                        i.getReservedQuantity()
                ))
                .toList();
    }

    public List<InventoryByProductResponse> getByProduct(Long productId) {
        return inventoryRepo.findByProductId(productId).stream()
                .map(i -> new InventoryByProductResponse(
                        i.getWarehouse().getId(),
                        i.getAvailableQuantity()
                ))
                .toList();
    }

    public List<LowStockResponse> getLowStock() {
        return inventoryRepo.findAll().stream()
                .filter(i -> i.getAvailableQuantity() <= i.getLowStockThreshold())
                .map(i -> new LowStockResponse(
                		i.getWarehouse().getId(),
                        i.getProductId(),
                        i.getCategory(),
                        i.getAvailableQuantity()
                ))
                .toList();
    }

    public void reserve(ReserveStockRequest req) {
        for (var item : req.getItems()) {
            Inventory inv = inventoryRepo
                    .findByProductIdAndWarehouseId(
                            item.getProductId(), req.getWarehouseId())
                    .orElseThrow(() -> new BusinessException("Inventory not found"));

            if (inv.getAvailableQuantity() < item.getQuantity())
                throw new BusinessException("Insufficient stock");

            inv.setAvailableQuantity(inv.getAvailableQuantity() - item.getQuantity());
            inv.setReservedQuantity(inv.getReservedQuantity() + item.getQuantity());

            reservationRepo.save(new StockReservation(
                    null,
                    req.getOrderId(),
                    item.getProductId(),
                    item.getQuantity(),
                    inv.getWarehouse(),
                    "RESERVED"
            ));
        }
    }

    public void release(Long orderId) {
        reservationRepo.findByOrderIdAndStatus(orderId, "RESERVED")
                .forEach(r -> {
                    Inventory inv = inventoryRepo
                            .findByProductIdAndWarehouseId(
                                    r.getProductId(), r.getWarehouse().getId())
                            .orElseThrow();

                    inv.setAvailableQuantity(inv.getAvailableQuantity() + r.getQuantity());
                    inv.setReservedQuantity(inv.getReservedQuantity() - r.getQuantity());
                    r.setStatus("RELEASED");
                });
    }

    public List<InventoryByCategoryResponse> getByCategory(String category) {

        return inventoryRepo.findByCategory(category).stream()
                .map(inv -> new InventoryByCategoryResponse(
                        inv.getProductId(),
                        inv.getCategory(),
                        inv.getWarehouse().getId(),
                        inv.getAvailableQuantity(),
                        inv.getReservedQuantity()
                ))
                .toList();
    }

    public void commit(Long orderId) {
        reservationRepo.findByOrderIdAndStatus(orderId, "RESERVED")
                .forEach(r -> r.setStatus("COMMITTED"));
    }

    private Inventory findInventory(Long id) {
        return inventoryRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Inventory not found"));
    }
}

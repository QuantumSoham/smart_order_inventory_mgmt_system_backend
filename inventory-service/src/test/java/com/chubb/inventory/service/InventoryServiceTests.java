package com.chubb.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.inventory.dto.request.AddInventoryRequest;
import com.chubb.inventory.dto.request.ReserveStockRequest;
import com.chubb.inventory.dto.request.UpdateInventoryRequest;
import com.chubb.inventory.entity.Inventory;
import com.chubb.inventory.entity.Product;
import com.chubb.inventory.entity.Warehouse;
import com.chubb.inventory.entity.StockReservation;
import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.repository.InventoryRepository;
import com.chubb.inventory.repository.WarehouseRepository;
import com.chubb.inventory.repository.StockReservationRepository;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepo;

    @Mock
    private WarehouseRepository warehouseRepo;

    @Mock
    private StockReservationRepository reservationRepo;

    @Mock
    private ProductService productService;

    @InjectMocks
    private InventoryService service;

    private Warehouse wh;
    private Product product;

    @BeforeEach
    void setup() {
        wh = new Warehouse();
        wh.setId(2L);
        wh.setName("W");

        product = new Product();
        product.setId(3L);
        product.setName("P");
        product.setCategory("C");
    }

    @Test
    void addStock_success() {
        AddInventoryRequest req = new AddInventoryRequest();
        req.setProductId(3L);
        req.setWarehouseId(2L);
        req.setQuantity(50);
        req.setLowStockThreshold(5);

        when(productService.getEntity(3L)).thenReturn(product);
        when(warehouseRepo.findById(2L)).thenReturn(Optional.of(wh));
        when(inventoryRepo.findByProductIdAndWarehouseId(3L, 2L)).thenReturn(Optional.empty());

        Inventory saved = new Inventory();
        saved.setId(7L);
        saved.setProductId(3L);
        saved.setWarehouse(wh);
        saved.setAvailableQuantity(50);

        when(inventoryRepo.save(any(Inventory.class))).thenReturn(saved);

        var resp = service.addStock(req);
        assertEquals(7L, resp.getInventoryId());
        assertEquals(50, resp.getAvailable());
    }

    @Test
    void addStock_duplicate_throws() {
        AddInventoryRequest req = new AddInventoryRequest();
        req.setProductId(3L);
        req.setWarehouseId(2L);
        req.setQuantity(10);

        when(productService.getEntity(3L)).thenReturn(product);
        when(warehouseRepo.findById(2L)).thenReturn(Optional.of(wh));

        Inventory existing = new Inventory();
        existing.setId(9L);
        when(inventoryRepo.findByProductIdAndWarehouseId(3L, 2L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class, () -> service.addStock(req));
    }

    @Test
    void reserve_and_insufficient() {
        ReserveStockRequest.ReserveItemRequest item = new ReserveStockRequest.ReserveItemRequest();
        item.setProductId(3L);
        item.setQuantity(5);

        ReserveStockRequest req = new ReserveStockRequest();
        req.setOrderId(100L);
        req.setWarehouseId(2L);
        req.setItems(List.of(item));

        Inventory inv = new Inventory();
        inv.setId(20L);
        inv.setProductId(3L);
        inv.setWarehouse(wh);
        inv.setAvailableQuantity(10);
        inv.setReservedQuantity(0);

        when(inventoryRepo.findByProductIdAndWarehouseId(3L, 2L)).thenReturn(Optional.of(inv));

        // successful reservation
        service.reserve(req);
        assertEquals(5, inv.getAvailableQuantity());
        assertEquals(5, inv.getReservedQuantity());
        verify(reservationRepo).save(any(StockReservation.class));

        // insufficient stock
        ReserveStockRequest.ReserveItemRequest item2 = new ReserveStockRequest.ReserveItemRequest();
        item2.setProductId(3L);
        item2.setQuantity(100);
        ReserveStockRequest req2 = new ReserveStockRequest();
        req2.setOrderId(101L);
        req2.setWarehouseId(2L);
        req2.setItems(List.of(item2));

        when(inventoryRepo.findByProductIdAndWarehouseId(3L, 2L)).thenReturn(Optional.of(inv));

        assertThrows(BusinessException.class, () -> service.reserve(req2));
    }

    @Test
    void release_and_commit() {
        StockReservation r = new StockReservation(null, 100L, 3L, 4, wh, "RESERVED");

        Inventory inv = new Inventory();
        inv.setId(30L);
        inv.setProductId(3L);
        inv.setWarehouse(wh);
        inv.setAvailableQuantity(6);
        inv.setReservedQuantity(4);

        when(reservationRepo.findByOrderIdAndStatus(100L, "RESERVED")).thenReturn(List.of(r));
        when(inventoryRepo.findByProductIdAndWarehouseId(3L, 2L)).thenReturn(Optional.of(inv));

        service.release(100L);
        assertEquals(10, inv.getAvailableQuantity());
        assertEquals(0, inv.getReservedQuantity());
        assertEquals("RELEASED", r.getStatus());

        // commit: set status to COMMITTED
        // create new reserved and test commit
        StockReservation r2 = new StockReservation(null, 200L, 3L, 2, wh, "RESERVED");
        when(reservationRepo.findByOrderIdAndStatus(200L, "RESERVED")).thenReturn(List.of(r2));

        service.commit(200L);
        assertEquals("COMMITTED", r2.getStatus());
    }

    @Test
    void updateStock_success_and_notFound() {
        Inventory inv = new Inventory();
        inv.setId(40L);
        inv.setProductId(3L);
        inv.setWarehouse(wh);
        inv.setReservedQuantity(5);
        inv.setAvailableQuantity(10);
        inv.setTotalQuantity(15);

        when(inventoryRepo.findById(40L)).thenReturn(Optional.of(inv));

        UpdateInventoryRequest req = new UpdateInventoryRequest();
        req.setCategory("NEWCAT");
        req.setQuantity(20);

        service.updateStock(40L, req);

        assertEquals("NEWCAT", inv.getCategory());
        assertEquals(20, inv.getTotalQuantity());
        // availableQuantity should be quantity - reserved
        assertEquals(15, inv.getAvailableQuantity());

        when(inventoryRepo.findById(999L)).thenReturn(Optional.empty());
        UpdateInventoryRequest req2 = new UpdateInventoryRequest();
        req2.setCategory("x"); req2.setQuantity(1);
        assertThrows(BusinessException.class, () -> service.updateStock(999L, req2));
    }

    @Test
    void getByWarehouse_getByProduct_getByCategory_and_getLowStock() {
        Inventory i1 = new Inventory();
        i1.setId(101L);
        i1.setProductId(3L);
        i1.setWarehouse(wh);
        i1.setCategory("C");
        i1.setAvailableQuantity(2);
        i1.setReservedQuantity(1);
        i1.setLowStockThreshold(5);

        Inventory i2 = new Inventory();
        i2.setId(102L);
        i2.setProductId(4L);
        Warehouse wh2 = new Warehouse(); wh2.setId(99L); i2.setWarehouse(wh2);
        i2.setCategory("C2");
        i2.setAvailableQuantity(50);
        i2.setReservedQuantity(0);
        i2.setLowStockThreshold(10);

        when(inventoryRepo.findByWarehouseId(2L)).thenReturn(List.of(i1));
        when(inventoryRepo.findByProductId(3L)).thenReturn(List.of(i1));
        when(inventoryRepo.findByCategory("C")).thenReturn(List.of(i1));
        when(inventoryRepo.findAll()).thenReturn(List.of(i1, i2));

        var byWh = service.getByWarehouse(2L);
        assertEquals(1, byWh.size());
        assertEquals(101L, byWh.get(0).getInventoryId());

        var byProd = service.getByProduct(3L);
        assertEquals(1, byProd.size());
        assertEquals(2, byProd.get(0).getAvailable());

        var byCat = service.getByCategory("C");
        assertEquals(1, byCat.size());
        assertEquals(3L, byCat.get(0).getProductId());

        var low = service.getLowStock();
        // only i1 has available <= lowStockThreshold
        assertEquals(1, low.size());
        assertEquals(3L, low.get(0).getProductId());
    }
}

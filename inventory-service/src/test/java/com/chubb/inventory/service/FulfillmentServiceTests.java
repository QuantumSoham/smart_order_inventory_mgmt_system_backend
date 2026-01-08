package com.chubb.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.inventory.dto.request.AssignFulfillmentRequest;
import com.chubb.inventory.dto.response.FulfillmentResponse;
import com.chubb.inventory.entity.Fulfillment;
import com.chubb.inventory.entity.Warehouse;
import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.repository.FulfillmentRepository;
import com.chubb.inventory.repository.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
class FulfillmentServiceTests {

    @Mock
    private FulfillmentRepository repo;

    @Mock
    private WarehouseRepository warehouseRepo;

    @InjectMocks
    private FulfillmentService service;

    @Test
    void assign_and_get_and_updateStatus() {
        AssignFulfillmentRequest req = new AssignFulfillmentRequest();
        req.setOrderId(11L);
        req.setWarehouseId(2L);

        Warehouse wh = new Warehouse();
        wh.setId(2L);
        wh.setName("W");

        when(warehouseRepo.findById(2L)).thenReturn(Optional.of(wh));

        service.assign(req);
        verify(repo).save(any(Fulfillment.class));

        Fulfillment f = new Fulfillment();
        f.setOrderId(11L);
        f.setWarehouse(wh);
        f.setStatus("PENDING");

        when(repo.findByOrderId(11L)).thenReturn(Optional.of(f));

        FulfillmentResponse resp = service.get(11L);
        assertEquals(11L, resp.getOrderId());

        service.updateStatus(11L, "SHIPPED");
        assertEquals("SHIPPED", f.getStatus());
        verify(repo, times(2)).save(any(Fulfillment.class));
    }

    @Test
    void updateStatus_notFound_throws() {
        when(repo.findByOrderId(99L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.updateStatus(99L, "X"));
    }
}

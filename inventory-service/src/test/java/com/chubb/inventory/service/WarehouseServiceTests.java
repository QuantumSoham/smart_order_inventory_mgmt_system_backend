package com.chubb.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.inventory.dto.request.WarehouseRequest;
import com.chubb.inventory.dto.response.WarehouseResponse;
import com.chubb.inventory.entity.Warehouse;
import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.repository.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTests {

    @Mock
    private WarehouseRepository repo;

    @InjectMocks
    private WarehouseService service;

    @Test
    void create_and_getAll_and_getById() {
        WarehouseRequest req = new WarehouseRequest();
        req.setName("W");
        req.setLocation("L");

        Warehouse w = new Warehouse();
        w.setId(10L);
        w.setName("W");
        w.setLocation("L");
        w.setActive(true);

        when(repo.save(any(Warehouse.class))).thenReturn(w);
        when(repo.findAll()).thenReturn(List.of(w));
        when(repo.findById(10L)).thenReturn(Optional.of(w));

        WarehouseResponse r = service.create(req);
        assertEquals(10L, r.getId());

        List<WarehouseResponse> all = service.getAll();
        assertEquals(1, all.size());

        WarehouseResponse byId = service.getById(10L);
        assertEquals("W", byId.getName());
    }

    @Test
    void find_missing_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.getById(99L));
    }

    @Test
    void update_changesAndSave() {
        Warehouse w = new Warehouse();
        w.setId(55L);
        w.setName("Old");
        w.setLocation("L1");

        when(repo.findById(55L)).thenReturn(Optional.of(w));

        WarehouseRequest req = new WarehouseRequest();
        req.setName("NewName");
        req.setLocation("NewLoc");

        service.update(55L, req);

        assertEquals("NewName", w.getName());
        assertEquals("NewLoc", w.getLocation());
        verify(repo).save(w);
    }

    @Test
    void disable_setsInactive() {
        Warehouse w = new Warehouse();
        w.setId(66L);
        w.setActive(true);

        when(repo.findById(66L)).thenReturn(Optional.of(w));

        service.disable(66L);

        assertFalse(w.isActive());
        verify(repo).save(w);
    }
}

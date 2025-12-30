package com.chubb.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chubb.inventory.exception.BusinessException;
import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;
import com.chubb.inventory.entity.*;
import com.chubb.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repo;

    public WarehouseResponse create(WarehouseRequest req) {
        Warehouse w = new Warehouse();
        w.setName(req.getName());
        w.setLocation(req.getLocation());
        w.setActive(true);

        return toResponse(repo.save(w));
    }

    public List<WarehouseResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public WarehouseResponse getById(Long id) {
        return toResponse(find(id));
    }

    public void update(Long id, WarehouseRequest req) {
        Warehouse w = find(id);
        w.setName(req.getName());
        w.setLocation(req.getLocation());
        repo.save(w);
    }

    public void disable(Long id) {
        Warehouse w = find(id);
        w.setActive(false);
        repo.save(w);
    }

    private Warehouse find(Long id) 
    {
    	return repo.findById(id)
                .orElseThrow(() -> new BusinessException("Warehouse not found"));
    }

    private WarehouseResponse toResponse(Warehouse w) {
        return new WarehouseResponse(
                w.getId(),
                w.getName(),
                w.getLocation(),
                w.isActive()
        );
    }
}

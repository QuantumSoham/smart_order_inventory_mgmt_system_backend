package com.chubb.inventory.service;

import org.springframework.stereotype.Service;

import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;
import com.chubb.inventory.entity.*;
import com.chubb.inventory.exception.*;
import com.chubb.inventory.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FulfillmentService {

    private final FulfillmentRepository repo;
    private final WarehouseRepository warehouseRepo;

    public void assign(AssignFulfillmentRequest req) {
        Warehouse wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new BusinessException("Warehouse not found"));

        repo.save(new Fulfillment(
                null,
                req.getOrderId(),
                wh,
                "PENDING"
        ));
    }

    public void updateStatus(Long orderId, String status) {
        Fulfillment f = repo.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Fulfillment not found"));

        f.setStatus(status);
        repo.save(f);
    }

    public FulfillmentResponse get(Long orderId) {
        Fulfillment f = repo.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Fulfillment not found"));

        return new FulfillmentResponse(
                f.getOrderId(),
                f.getWarehouse().getId(),
                f.getStatus()
        );
    }
}

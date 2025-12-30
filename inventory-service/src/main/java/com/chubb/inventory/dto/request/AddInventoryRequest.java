package com.chubb.inventory.dto.request;

import lombok.*;

@Getter @Setter
public class AddInventoryRequest {
    private Long productId;
    private Long warehouseId;
    private int quantity;
    private int lowStockThreshold;
}


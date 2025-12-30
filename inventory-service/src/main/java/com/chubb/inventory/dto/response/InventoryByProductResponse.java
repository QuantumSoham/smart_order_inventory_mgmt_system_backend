package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class InventoryByProductResponse {
    private Long warehouseId;
    private int available;
}

package com.chubb.inventory.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class InventoryByCategoryResponse {
    private Long productId;
    private String category;
    private Long warehouseId;
    private int available;
    private int reserved;
}

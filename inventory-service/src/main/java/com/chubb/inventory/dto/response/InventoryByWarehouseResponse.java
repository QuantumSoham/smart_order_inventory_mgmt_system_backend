package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class InventoryByWarehouseResponse {
    private Long productId;
    private int available;
    private int reserved;
}

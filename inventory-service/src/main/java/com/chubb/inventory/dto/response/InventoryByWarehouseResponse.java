package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class InventoryByWarehouseResponse {
	private Long inventoryId;
    private Long productId;
    private String category;
    private int available;
    private int reserved;
}

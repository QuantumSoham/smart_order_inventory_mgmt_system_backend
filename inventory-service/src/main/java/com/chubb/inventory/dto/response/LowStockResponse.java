package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class LowStockResponse {
	private Long warehouseId;
    private Long productId;
    private String category;
    private int available;
}

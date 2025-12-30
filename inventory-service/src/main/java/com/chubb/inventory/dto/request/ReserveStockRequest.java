package com.chubb.inventory.dto.request;

import lombok.*;
import java.util.List;

@Getter @Setter
public class ReserveStockRequest {

    private Long orderId;
    private Long warehouseId;
    private List<ReserveItemRequest> items;

    @Getter @Setter
    public static class ReserveItemRequest {
        private Long productId;
        private int quantity;
    }
}

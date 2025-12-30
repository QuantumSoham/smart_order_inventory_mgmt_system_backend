package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class FulfillmentResponse {
    private Long orderId;
    private Long warehouseId;
    private String status;
}

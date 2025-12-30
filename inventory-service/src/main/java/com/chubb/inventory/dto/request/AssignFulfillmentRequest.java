package com.chubb.inventory.dto.request;

import lombok.*;

@Getter @Setter
public class AssignFulfillmentRequest {
    private Long orderId;
    private Long warehouseId;
}


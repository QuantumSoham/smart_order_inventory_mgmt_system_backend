package com.chubb.order.dto.request;

import lombok.*;

@Getter @Setter
public class OrderItemRequest {
    private Long productId;
    private int quantity;
}


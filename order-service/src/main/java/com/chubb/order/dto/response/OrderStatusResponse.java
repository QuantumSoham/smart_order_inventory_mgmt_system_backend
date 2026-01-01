package com.chubb.order.dto.response;

import com.chubb.order.entity.OrderStatus;

import lombok.*;

@AllArgsConstructor
@Getter
public class OrderStatusResponse {
    private Long orderId;
    private OrderStatus status;
}

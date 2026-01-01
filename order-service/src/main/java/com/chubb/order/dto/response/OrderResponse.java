package com.chubb.order.dto.response;

import java.math.BigDecimal;

import com.chubb.order.entity.OrderStatus;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
}

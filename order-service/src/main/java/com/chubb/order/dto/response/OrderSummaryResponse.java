package com.chubb.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.chubb.order.entity.OrderStatus;

import lombok.*;

@AllArgsConstructor
@Getter
public class OrderSummaryResponse {
    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}

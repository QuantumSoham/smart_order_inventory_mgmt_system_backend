package com.chubb.order.dto.analytics;

import com.chubb.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusCountDto {
    private OrderStatus status;
    private Long count;
}

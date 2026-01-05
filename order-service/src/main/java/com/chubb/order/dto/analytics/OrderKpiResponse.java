package com.chubb.order.dto.analytics;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderKpiResponse {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal avgOrderValue;
    private Long deliveredOrders;
}

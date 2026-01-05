package com.chubb.order.dto.analytics;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseRevenueDto {
    private Long warehouseId;
    private BigDecimal revenue;
}

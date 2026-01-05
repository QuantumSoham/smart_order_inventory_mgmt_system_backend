package com.chubb.order.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseOrderCountDto {
    private Long warehouseId;
    private Long count;
}

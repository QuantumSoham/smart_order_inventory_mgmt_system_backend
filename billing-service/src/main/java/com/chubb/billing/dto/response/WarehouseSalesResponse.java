package com.chubb.billing.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@AllArgsConstructor
public class WarehouseSalesResponse {
    private Long warehouseId;
    private BigDecimal revenue;
}

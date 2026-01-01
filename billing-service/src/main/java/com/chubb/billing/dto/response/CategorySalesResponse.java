package com.chubb.billing.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@AllArgsConstructor
public class CategorySalesResponse {
    private String category;
    private BigDecimal revenue;
}

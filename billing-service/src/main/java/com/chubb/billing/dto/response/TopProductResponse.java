package com.chubb.billing.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@AllArgsConstructor
public class TopProductResponse {
    private String productName;
    private BigDecimal revenue;
}

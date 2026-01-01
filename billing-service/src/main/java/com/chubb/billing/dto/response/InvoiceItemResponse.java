package com.chubb.billing.dto.response;

import java.math.BigDecimal;

import lombok.*;

@AllArgsConstructor
@Getter
public class InvoiceItemResponse {

    private String productName;
    private String category;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}

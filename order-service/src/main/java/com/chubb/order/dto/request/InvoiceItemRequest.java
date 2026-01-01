package com.chubb.order.dto.request;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvoiceItemRequest {

    private Long productId;
    private String productName;
    private String category;
    private int quantity;
    private BigDecimal unitPrice;
}

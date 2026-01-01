package com.chubb.order.dto.response;

import java.math.BigDecimal;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvoiceResponse {
    private Long invoiceId;
    private BigDecimal totalAmount;
    private String status;
}

package com.chubb.billing.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter @Setter
public class CreateInvoiceRequest {

    private Long orderId;
    private Long userId;
    private Long warehouseId;
    private BigDecimal totalAmount;

    private List<InvoiceItemRequest> items;
}

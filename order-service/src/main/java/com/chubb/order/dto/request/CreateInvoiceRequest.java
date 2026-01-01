package com.chubb.order.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateInvoiceRequest {

    private Long orderId;
    private Long userId;
    private Long warehouseId;
    private BigDecimal totalAmount;
    private List<InvoiceItemRequest> items;
}

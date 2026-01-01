package com.chubb.billing.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.chubb.billing.entity.InvoiceStatus;

import lombok.*;

@AllArgsConstructor
@Getter
public class InvoiceResponse {

    private Long invoiceId;
    private InvoiceStatus status;
    private BigDecimal totalAmount;
    private List<InvoiceItemResponse> items;
}

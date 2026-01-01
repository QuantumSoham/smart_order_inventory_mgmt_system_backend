package com.chubb.billing.dto.response;

import java.time.LocalDateTime;

import com.chubb.billing.entity.InvoiceStatus;

import lombok.*;

@AllArgsConstructor
@Getter
public class PaymentResponse {

    private Long invoiceId;
    private InvoiceStatus status;
    private LocalDateTime paidAt;
}

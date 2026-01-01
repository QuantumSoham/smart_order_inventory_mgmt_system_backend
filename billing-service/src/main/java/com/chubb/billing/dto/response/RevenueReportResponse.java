package com.chubb.billing.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@AllArgsConstructor
public class RevenueReportResponse {
    private String date;
    private BigDecimal revenue;
}

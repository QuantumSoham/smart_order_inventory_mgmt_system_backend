package com.chubb.order.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RevenueByDateDto {
    private LocalDate date;
    private BigDecimal revenue;
}

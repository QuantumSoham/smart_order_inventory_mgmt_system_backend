package com.chubb.order.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdersByStateDto {
    private String state;
    private Long count;
}

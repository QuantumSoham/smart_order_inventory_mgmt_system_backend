package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class StockActionResponse {
    private String status;
    private Long orderId;
}

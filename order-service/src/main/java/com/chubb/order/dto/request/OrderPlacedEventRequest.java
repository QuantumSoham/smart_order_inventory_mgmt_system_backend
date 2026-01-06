package com.chubb.order.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEventRequest {

    private Long orderId;
    private Long userId;
    private String userEmail;
    private String status;
    private Double totalAmount;
}

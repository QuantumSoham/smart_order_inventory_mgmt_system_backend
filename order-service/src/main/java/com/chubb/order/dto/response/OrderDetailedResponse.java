package com.chubb.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.chubb.order.entity.OrderStatus;

import lombok.*;

@Getter
@AllArgsConstructor
public class OrderDetailedResponse {

    private Long id;
    private Long userId;
    private Long warehouseId;

    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String city;
    private String state;
    private String pincode;

    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    
}

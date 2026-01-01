package com.chubb.order.dto.request;

import java.util.List;
import lombok.*;

@Getter @Setter
public class CreateOrderRequest {

    private Long userId;        // from gateway later
    private Long warehouseId;

    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String city;
    private String state;
    private String pincode;

    private List<OrderItemRequest> items;
}

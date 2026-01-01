package com.chubb.order.entity;


import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;
    private BigDecimal priceAtPurchase;

    @ManyToOne
    private Order order;
}

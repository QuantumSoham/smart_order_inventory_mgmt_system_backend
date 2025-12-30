package com.chubb.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long productId;
    private int quantity;

    @ManyToOne
    private Warehouse warehouse;

    private String status; // RESERVED, RELEASED, COMMITTED
}

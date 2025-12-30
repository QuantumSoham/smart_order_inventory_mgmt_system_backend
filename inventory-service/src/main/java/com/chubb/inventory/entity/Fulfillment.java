package com.chubb.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Fulfillment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @ManyToOne
    private Warehouse warehouse;

    private String status; // PENDING, PACKED, DISPATCHED, DELIVERED
}

package com.chubb.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"productId", "warehouse_id"})
)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @ManyToOne
    private Warehouse warehouse;

    private String category;
    private int totalQuantity;
    private int availableQuantity;
    private int reservedQuantity;
    private int lowStockThreshold;
}

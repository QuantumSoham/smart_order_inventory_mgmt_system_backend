package com.chubb.inventory.dto.request;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private BigDecimal price;
}

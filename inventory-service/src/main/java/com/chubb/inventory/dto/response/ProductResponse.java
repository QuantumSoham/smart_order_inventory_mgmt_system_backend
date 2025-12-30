package com.chubb.inventory.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
}

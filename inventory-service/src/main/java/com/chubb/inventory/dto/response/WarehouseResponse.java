package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class WarehouseResponse {
    private Long id;
    private String name;
    private String location;
    private boolean isActive;
}

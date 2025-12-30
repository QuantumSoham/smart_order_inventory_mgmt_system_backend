package com.chubb.inventory.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class AddInventoryResponse {
    private Long inventoryId;
    private int available;
}

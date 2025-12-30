package com.chubb.inventory.dto.request;

import lombok.*;

@Getter @Setter
public class UpdateInventoryRequest {
	private String category;
	private int quantity;
}

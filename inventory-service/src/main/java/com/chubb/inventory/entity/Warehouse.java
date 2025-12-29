package com.chubb.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Warehouse {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String location;
  private boolean isActive = true;
}

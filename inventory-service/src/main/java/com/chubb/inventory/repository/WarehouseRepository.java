package com.chubb.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chubb.inventory.entity.Warehouse;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByActiveTrue();
}


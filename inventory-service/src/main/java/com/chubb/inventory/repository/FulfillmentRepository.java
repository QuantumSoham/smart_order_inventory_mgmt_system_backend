package com.chubb.inventory.repository;


import com.chubb.inventory.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FulfillmentRepository
        extends JpaRepository<Fulfillment, Long> {

    Optional<Fulfillment> findByOrderId(Long orderId);
}

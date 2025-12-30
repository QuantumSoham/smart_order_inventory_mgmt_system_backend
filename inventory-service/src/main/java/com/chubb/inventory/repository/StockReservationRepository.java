package com.chubb.inventory.repository;

import com.chubb.inventory.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockReservationRepository
        extends JpaRepository<StockReservation, Long> {

    // Used to fetch all reservations for an order
    List<StockReservation> findByOrderId(Long orderId);

    // Used for safe release & commit (ONLY RESERVED)
    List<StockReservation> findByOrderIdAndStatus(
            Long orderId,
            String status
    );
}

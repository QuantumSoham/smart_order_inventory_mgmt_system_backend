package com.chubb.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chubb.order.entity.Order;
import com.chubb.order.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);
}

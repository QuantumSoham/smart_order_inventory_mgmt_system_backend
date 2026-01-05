package com.chubb.order.repository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chubb.order.dto.analytics.RevenueByDateDto;
import com.chubb.order.entity.Order;
import com.chubb.order.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);
//    Optional findById(Long orderId);
    // 🔹 KPI Summary
    @Query("""
            SELECT COUNT(o), COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
        """)
        Object fetchOrderCountAndRevenue();

        @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.status = 'DELIVERED'
        """)
        Long countDeliveredOrders();

    // 🔹 Orders by Status
    @Query("""
        SELECT o.status, COUNT(o)
        FROM Order o
        GROUP BY o.status
    """)
    List<Object[]> countByStatus();

    // 🔹 Orders by Warehouse
    @Query("""
        SELECT o.warehouseId, COUNT(o)
        FROM Order o
        GROUP BY o.warehouseId
    """)
    List<Object[]> countByWarehouse();

    // 🔹 Revenue by Warehouse
    @Query("""
        SELECT o.warehouseId, SUM(o.totalAmount)
        FROM Order o
        GROUP BY o.warehouseId
    """)
    List<Object[]> revenueByWarehouse();

    // 🔹 Revenue by Date
    @Query("""
    	    SELECT new com.chubb.order.dto.analytics.RevenueByDateDto(CAST(o.createdAt AS localdate), SUM(o.totalAmount))
    	    FROM Order o
    	    WHERE o.createdAt BETWEEN :start AND :end
    	    GROUP BY CAST(o.createdAt AS localdate)
    	""")
    	List<RevenueByDateDto> fetchRevenueByDate(LocalDateTime start, LocalDateTime end);
//    @Query("""
//        SELECT DATE(o.createdAt), SUM(o.totalAmount)
//        FROM Order o
//        WHERE o.createdAt BETWEEN :from AND :to
//        GROUP BY DATE(o.createdAt)
//        ORDER BY DATE(o.createdAt)
//    """)
//    List<Object[]> revenueByDate(
//        @Param("from") LocalDateTime from,
//        @Param("to") LocalDateTime to
//    );

    // 🔹 Orders by State
    @Query("""
        SELECT o.state, COUNT(o)
        FROM Order o
        GROUP BY o.state
    """)
    List<Object[]> countByState();
}

package com.chubb.billing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceAnalyticsRepository {

    @Query("""
        SELECT DATE(i.createdAt), SUM(i.totalAmount)
        FROM Invoice i
        WHERE i.status = 'PAID'
        AND i.createdAt BETWEEN :from AND :to
        GROUP BY DATE(i.createdAt)
    """)
    List<Object[]> revenueByDate(
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );

    @Query("""
        SELECT ii.category, SUM(ii.lineTotal)
        FROM InvoiceItem ii
        JOIN ii.invoice i
        WHERE i.status = 'PAID'
        GROUP BY ii.category
    """)
    List<Object[]> salesByCategory();

    @Query("""
        SELECT i.warehouseId, SUM(ii.lineTotal)
        FROM InvoiceItem ii
        JOIN ii.invoice i
        WHERE i.status = 'PAID'
        GROUP BY i.warehouseId
    """)
    List<Object[]> salesByWarehouse();

    @Query("""
        SELECT ii.productName, SUM(ii.lineTotal)
        FROM InvoiceItem ii
        JOIN ii.invoice i
        WHERE i.status = 'PAID'
        GROUP BY ii.productName
        ORDER BY SUM(ii.lineTotal) DESC
    """)
    List<Object[]> topProducts();
}


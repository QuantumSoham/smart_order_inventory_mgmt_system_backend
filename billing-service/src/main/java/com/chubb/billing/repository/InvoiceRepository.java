package com.chubb.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chubb.billing.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, InvoiceAnalyticsRepository {
    Optional<Invoice> findByOrderId(Long orderId);
}

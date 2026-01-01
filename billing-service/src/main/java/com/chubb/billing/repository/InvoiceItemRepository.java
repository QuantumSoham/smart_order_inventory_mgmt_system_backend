package com.chubb.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chubb.billing.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> 
{
	
}

package com.chubb.billing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.billing.dto.request.CreateInvoiceRequest;
import com.chubb.billing.dto.request.InvoiceItemRequest;
import com.chubb.billing.dto.response.InvoiceResponse;
import com.chubb.billing.dto.response.PaymentResponse;
import com.chubb.billing.entity.Invoice;
import com.chubb.billing.entity.InvoiceItem;
import com.chubb.billing.entity.InvoiceStatus;
import com.chubb.billing.exception.BusinessException;
import com.chubb.billing.repository.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private InvoiceRepository invoiceRepo;

    @InjectMocks
    private BillingService billingService;

    @Test
    void createInvoice_success() {

        CreateInvoiceRequest req = new CreateInvoiceRequest();
        req.setOrderId(1L);
        req.setUserId(10L);
        req.setWarehouseId(5L);
        req.setTotalAmount(BigDecimal.valueOf(1000));

        InvoiceItemRequest item = new InvoiceItemRequest();
        item.setProductId(101L);
        item.setProductName("Mouse");
        item.setCategory("ELECTRONICS");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(500));

        req.setItems(List.of(item));

        when(invoiceRepo.save(any(Invoice.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        InvoiceResponse response = billingService.createInvoice(req);

        assertNotNull(response);
        assertEquals(InvoiceStatus.UNPAID, response.getStatus());
        assertEquals(BigDecimal.valueOf(1000), response.getTotalAmount());
        assertEquals(1, response.getItems().size());
    }

    @Test
    void getInvoice_success() {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setOrderId(100L);
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setTotalAmount(BigDecimal.valueOf(800));
        invoice.setItems(List.of());

        when(invoiceRepo.findByOrderId(100L))
                .thenReturn(Optional.of(invoice));

        InvoiceResponse response = billingService.getInvoice(100L);

        assertEquals(InvoiceStatus.UNPAID, response.getStatus());
        assertEquals(BigDecimal.valueOf(800), response.getTotalAmount());
    }

    @Test
    void getInvoice_notFound() {

        when(invoiceRepo.findByOrderId(100L))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> billingService.getInvoice(100L)
        );

        assertEquals("Invoice not found", ex.getMessage());
    }

    @Test
    void payInvoice_success() {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setStatus(InvoiceStatus.UNPAID);

        when(invoiceRepo.findById(1L))
                .thenReturn(Optional.of(invoice));

        PaymentResponse response = billingService.pay(1L);

        assertEquals(InvoiceStatus.PAID, response.getStatus());
        assertNotNull(response.getPaidAt());
    }

    @Test
    void payInvoice_notFound() {

        when(invoiceRepo.findById(1L))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> billingService.pay(1L)
        );

        assertEquals("Invoice not found", ex.getMessage());
    }
}

package com.chubb.billing.service;

import com.chubb.billing.dto.request.CreateInvoiceRequest;
import com.chubb.billing.dto.request.InvoiceItemRequest;
import com.chubb.billing.dto.response.InvoiceResponse;
import com.chubb.billing.dto.response.PaymentResponse;
import com.chubb.billing.entity.Invoice;
import com.chubb.billing.entity.InvoiceItem;
import com.chubb.billing.entity.InvoiceStatus;
import com.chubb.billing.exception.BusinessException;
import com.chubb.billing.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    InvoiceRepository invoiceRepo;

    @InjectMocks
    BillingService billingService;

    @Test
    void createInvoice_createsAndSavesInvoice() {
        CreateInvoiceRequest req = new CreateInvoiceRequest();
        req.setOrderId(100L);
        req.setUserId(10L);
        req.setWarehouseId(5L);
        req.setTotalAmount(new BigDecimal("30.00"));

        InvoiceItemRequest itemReq = new InvoiceItemRequest();
        itemReq.setProductId(1L);
        itemReq.setProductName("Widget");
        itemReq.setCategory("Tools");
        itemReq.setQuantity(3);
        itemReq.setUnitPrice(new BigDecimal("10.00"));
        req.setItems(List.of(itemReq));

        when(invoiceRepo.save(any(Invoice.class))).thenAnswer(inv -> {
            Invoice i = inv.getArgument(0);
            i.setId(42L);
            return i;
        });

        InvoiceResponse resp = billingService.createInvoice(req);

        assertThat(resp.getInvoiceId()).isEqualTo(42L);
        assertThat(resp.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(resp.getTotalAmount()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(resp.getItems()).hasSize(1);
        assertThat(resp.getItems().get(0).getProductName()).isEqualTo("Widget");

        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepo, times(1)).save(captor.capture());
        Invoice saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(100L);
        assertThat(saved.getItems()).hasSize(1);
        assertThat(saved.getItems().get(0).getLineTotal()).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void getInvoice_found_returnsResponse() {
        Invoice invoice = new Invoice();
        invoice.setId(2L);
        invoice.setOrderId(200L);
        invoice.setStatus(InvoiceStatus.UNPAID);
        InvoiceItem it = new InvoiceItem();
        it.setProductName("Gadget");
        it.setCategory("Electronics");
        it.setQuantity(2);
        it.setUnitPrice(new BigDecimal("5.00"));
        it.setLineTotal(new BigDecimal("10.00"));
        it.setInvoice(invoice);
        invoice.setItems(List.of(it));

        when(invoiceRepo.findByOrderId(200L)).thenReturn(Optional.of(invoice));

        InvoiceResponse resp = billingService.getInvoice(200L);

        assertThat(resp.getInvoiceId()).isEqualTo(2L);
        assertThat(resp.getItems()).hasSize(1);
        assertThat(resp.getItems().get(0).getProductName()).isEqualTo("Gadget");
    }

    @Test
    void getInvoice_notFound_throws() {
        when(invoiceRepo.findByOrderId(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> billingService.getInvoice(999L));
    }

    @Test
    void pay_setsStatusAndPaidAt() {
        Invoice invoice = new Invoice();
        invoice.setId(7L);
        invoice.setStatus(InvoiceStatus.UNPAID);

        when(invoiceRepo.findById(7L)).thenReturn(Optional.of(invoice));

        PaymentResponse resp = billingService.pay(7L);

        assertThat(resp.getInvoiceId()).isEqualTo(7L);
        assertThat(resp.getStatus()).isEqualTo(InvoiceStatus.PAID);
        assertThat(resp.getPaidAt()).isNotNull();
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
        assertThat(invoice.getPaidAt()).isNotNull();
    }

    @Test
    void pay_notFound_throws() {
        when(invoiceRepo.findById(1234L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> billingService.pay(1234L));
    }
}

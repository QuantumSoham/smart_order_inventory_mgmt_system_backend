package com.chubb.billing.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.chubb.billing.dto.request.CreateInvoiceRequest;
import com.chubb.billing.dto.request.InvoiceItemRequest;
import com.chubb.billing.dto.response.InvoiceItemResponse;
import com.chubb.billing.dto.response.InvoiceResponse;
import com.chubb.billing.dto.response.PaymentResponse;
import com.chubb.billing.entity.Invoice;
import com.chubb.billing.entity.InvoiceItem;
import com.chubb.billing.entity.InvoiceStatus;
import com.chubb.billing.exception.BusinessException;
import com.chubb.billing.repository.InvoiceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingService {

    private final InvoiceRepository invoiceRepo;

    public InvoiceResponse createInvoice(CreateInvoiceRequest req) {

        Invoice invoice = new Invoice();
        invoice.setOrderId(req.getOrderId());
        invoice.setUserId(req.getUserId());
        invoice.setWarehouseId(req.getWarehouseId());
        invoice.setTotalAmount(req.getTotalAmount());
        invoice.setStatus(InvoiceStatus.UNPAID);

        List<InvoiceItem> items = new ArrayList<>();

        for (InvoiceItemRequest r : req.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setProductId(r.getProductId());
            item.setProductName(r.getProductName());
            item.setCategory(r.getCategory());
            item.setQuantity(r.getQuantity());
            item.setUnitPrice(r.getUnitPrice());
            item.setLineTotal(
                r.getUnitPrice().multiply(BigDecimal.valueOf(r.getQuantity()))
            );
            item.setInvoice(invoice);
            items.add(item);
        }

        invoice.setItems(items);
        invoiceRepo.save(invoice);

        return toResponse(invoice);
    }

    public InvoiceResponse getInvoice(Long orderId) {
        Invoice invoice = invoiceRepo.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Invoice not found"));
        return toResponse(invoice);
    }

    public PaymentResponse pay(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new BusinessException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());

        return new PaymentResponse(
                invoice.getId(),
                invoice.getStatus(),
                invoice.getPaidAt()
        );
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        List<InvoiceItemResponse> items =
                invoice.getItems().stream()
                        .map(i -> new InvoiceItemResponse(
                                i.getProductName(),
                                i.getCategory(),
                                i.getQuantity(),
                                i.getUnitPrice(),
                                i.getLineTotal()
                        ))
                        .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getStatus(),
                invoice.getTotalAmount(),
                items
        );
    }
}

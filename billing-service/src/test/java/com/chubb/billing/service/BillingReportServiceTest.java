package com.chubb.billing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.billing.dto.response.*;
import com.chubb.billing.repository.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class BillingReportServiceTest {

    @Mock
    private InvoiceRepository invoiceRepo;

    @InjectMocks
    private BillingReportService reportService;

    @Test
    void revenueByDate_success() {

        when(invoiceRepo.revenueByDate(any(), any()))
                .thenReturn(List.of(
                        new Object[]{"2025-01-01", BigDecimal.valueOf(5000)},
                        new Object[]{"2025-01-02", BigDecimal.valueOf(7000)}
                ));

        List<RevenueReportResponse> result =
                reportService.revenueByDate(
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 1, 2)
                );

        assertEquals(2, result.size());
        assertEquals("2025-01-01", result.get(0).getDate());
        assertEquals(BigDecimal.valueOf(5000), result.get(0).getRevenue());
    }
}

    
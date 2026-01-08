package com.chubb.billing.service;

import com.chubb.billing.dto.response.CategorySalesResponse;
import com.chubb.billing.dto.response.RevenueReportResponse;
import com.chubb.billing.dto.response.TopProductResponse;
import com.chubb.billing.dto.response.WarehouseSalesResponse;
import com.chubb.billing.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingReportServiceTest {

    @Mock
    InvoiceRepository invoiceRepo;

    @InjectMocks
    BillingReportService reportService;

    @Test
    void revenueByDate_mapsResults() {
        Object[] row = new Object[]{Date.valueOf(LocalDate.of(2025,1,1)), new BigDecimal("123.45")};
        ArrayList<Object[]> revenueRows = new ArrayList<>();
        revenueRows.add(row);
        when(invoiceRepo.revenueByDate(
            any(), any()
        )).thenReturn(revenueRows);

        List<RevenueReportResponse> out = reportService.revenueByDate(LocalDate.of(2025,1,1), LocalDate.of(2025,1,1));

        assertThat(out).hasSize(1);
        assertThat(out.get(0).getDate()).isEqualTo(row[0].toString());
        assertThat(out.get(0).getRevenue()).isEqualByComparingTo(new BigDecimal("123.45"));
    }

    @Test
    void salesByCategory_mapsResults() {
        Object[] row = new Object[]{"CatA", new BigDecimal("200.00")};
        ArrayList<Object[]> catRows = new ArrayList<>();
        catRows.add(row);
        when(invoiceRepo.salesByCategory()).thenReturn(catRows);

        List<CategorySalesResponse> out = reportService.salesByCategory();
        assertThat(out).hasSize(1);
        assertThat(out.get(0).getCategory()).isEqualTo("CatA");
        assertThat(out.get(0).getRevenue()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void salesByWarehouse_mapsResults() {
        Object[] row = new Object[]{1L, new BigDecimal("350.00")};
        ArrayList<Object[]> whRows = new ArrayList<>();
        whRows.add(row);
        when(invoiceRepo.salesByWarehouse()).thenReturn(whRows);

        List<WarehouseSalesResponse> out = reportService.salesByWarehouse();
        assertThat(out).hasSize(1);
        assertThat(out.get(0).getWarehouseId()).isEqualTo(1L);
        assertThat(out.get(0).getRevenue()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void topProducts_mapsResults() {
        Object[] row = new Object[]{"TopThing", new BigDecimal("999.99")};
        ArrayList<Object[]> topRows = new ArrayList<>();
        topRows.add(row);
        when(invoiceRepo.topProducts()).thenReturn(topRows);

        List<TopProductResponse> out = reportService.topProducts();
        assertThat(out).hasSize(1);
        assertThat(out.get(0).getProductName()).isEqualTo("TopThing");
        assertThat(out.get(0).getRevenue()).isEqualByComparingTo(new BigDecimal("999.99"));
    }
}

//package com.chubb.order.service;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.*;
//import java.util.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.chubb.order.dto.request.*;
//import com.chubb.order.dto.response.*;
//import com.chubb.order.entity.*;
//import com.chubb.order.exception.BusinessException;
//import com.chubb.order.feignclient.BillingClient;
//import com.chubb.order.feignclient.InventoryClient;
//import com.chubb.order.repository.OrderRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.stereotype.Service;
//import lombok.RequiredArgsConstructor;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.chubb.order.dto.analytics.*;
//import com.chubb.order.entity.OrderStatus;
//import com.chubb.order.repository.OrderRepository;
//
//@ExtendWith(MockitoExtension.class)
//class OrderAnalyticsServiceTest {
//
//    @Mock
//    private OrderRepository repo;
//
//    @InjectMocks
//    private OrderAnalyticsService analyticsService;
//
//    @Test
//    void getKpis_success() {
//        when(repo.fetchOrderCountAndRevenue())
//                .thenReturn(new Object[]{5L, BigDecimal.valueOf(1000)});
//        when(repo.countDeliveredOrders()).thenReturn(3L);
//
//        OrderKpiResponse res = analyticsService.getKpis();
//
//        assertEquals(5L, res.totalOrders());
//        assertEquals(BigDecimal.valueOf(1000), res.totalRevenue());
//        assertEquals(BigDecimal.valueOf(200.00), res.avgOrderValue());
//        assertEquals(3L, res.deliveredOrders());
//    }
//
//    @Test
//    void ordersByStatus() {
//        when(repo.countByStatus()).thenReturn(
//                List.of(new Object[]{OrderStatus.CREATED, 4L})
//        );
//
//        List<OrderStatusCountDto> res =
//                analyticsService.ordersByStatus();
//
//        assertEquals(1, res.size());
//        assertEquals(OrderStatus.CREATED, res.get(0).status());
//    }
//
//    @Test
//    void ordersByWarehouse() {
//        when(repo.countByWarehouse())
//                .thenReturn(List.of(new Object[]{1L, 10L}));
//
//        List<WarehouseOrderCountDto> res =
//                analyticsService.ordersByWarehouse();
//
//        assertEquals(1L, res.get(0).warehouseId());
//    }
//
//    @Test
//    void revenueByWarehouse() {
//        when(repo.revenueByWarehouse())
//                .thenReturn(List.of(
//                        new Object[]{1L, BigDecimal.valueOf(5000)}
//                ));
//
//        List<WarehouseRevenueDto> res =
//                analyticsService.revenueByWarehouse();
//
//        assertEquals(BigDecimal.valueOf(5000), res.get(0).revenue());
//    }
//
//    @Test
//    void ordersByState() {
//        when(repo.countByState())
//                .thenReturn(List.of(
//                        new Object[]{"DL", 20L}
//                ));
//
//        List<OrdersByStateDto> res =
//                analyticsService.ordersByState();
//
//        assertEquals("DL", res.get(0).state());
//        assertEquals(20L, res.get(0).count());
//    }
//}

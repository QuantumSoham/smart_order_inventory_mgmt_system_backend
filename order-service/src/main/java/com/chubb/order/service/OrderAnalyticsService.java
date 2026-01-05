package com.chubb.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.chubb.order.dto.analytics.*;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderAnalyticsService {

    private final OrderRepository repo;

    public OrderKpiResponse getKpis() {
        // result will be an Object[] containing [count, sum]
        Object result = repo.fetchOrderCountAndRevenue();

        if (result == null) {
            return new OrderKpiResponse(0L, BigDecimal.ZERO, BigDecimal.ZERO, 0L);
        }

        Object[] row = (Object[]) result;
        
        // Now row[0] is the Long (count) and row[1] is the BigDecimal (sum)
        Long totalOrders = ((Number) row[0]).longValue();
        BigDecimal totalRevenue = (BigDecimal) row[1];

        BigDecimal avgOrderValue = totalOrders == 0
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);

        Long deliveredOrders = repo.countDeliveredOrders();

        return new OrderKpiResponse(
                totalOrders,
                totalRevenue,
                avgOrderValue,
                deliveredOrders
        );
    }

    public List<OrderStatusCountDto> ordersByStatus() {
        return repo.countByStatus().stream()
            .map(r -> new OrderStatusCountDto(
                (OrderStatus) r[0],
                (Long) r[1]
            ))
            .toList();
    }

    public List<WarehouseOrderCountDto> ordersByWarehouse() {
        return repo.countByWarehouse().stream()
            .map(r -> new WarehouseOrderCountDto(
                (Long) r[0],
                (Long) r[1]
            ))
            .toList();
    }

    public List<WarehouseRevenueDto> revenueByWarehouse() {
        return repo.revenueByWarehouse().stream()
            .map(r -> new WarehouseRevenueDto(
                (Long) r[0],
                (BigDecimal) r[1]
            ))
            .toList();
    }
    public List<RevenueByDateDto> revenueByDate(LocalDate from, LocalDate to) {
        return repo.fetchRevenueByDate(
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );
    }
//    public List<RevenueByDateDto> revenueByDate(LocalDate from, LocalDate to) {
//        return repo.revenueByDate(
//                from.atStartOfDay(),
//                to.atTime(23, 59, 59)
//        ).stream()
//         .map(r -> {
//             // Convert java.sql.Date to java.time.LocalDate
//             LocalDate date = (r[0] instanceof java.sql.Date sqlDate) 
//                              ? sqlDate.toLocalDate() 
//                              : (LocalDate) r[0]; // Fallback if already LocalDate
//                              
//             return new RevenueByDateDto(
//                 date,
//                 (BigDecimal) r[1]
//             );
//         })
//         .toList();
//    }

    public List<OrdersByStateDto> ordersByState() {
        return repo.countByState().stream()
            .map(r -> new OrdersByStateDto(
                (String) r[0],
                (Long) r[1]
            ))
            .toList();
    }
}

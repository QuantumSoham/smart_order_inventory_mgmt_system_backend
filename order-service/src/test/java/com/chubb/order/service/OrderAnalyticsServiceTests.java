package com.chubb.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import com.chubb.order.dto.analytics.OrderKpiResponse;
import com.chubb.order.dto.analytics.OrderStatusCountDto;
import com.chubb.order.dto.analytics.OrdersByStateDto;
import com.chubb.order.dto.analytics.WarehouseOrderCountDto;
import com.chubb.order.dto.analytics.WarehouseRevenueDto;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)

class OrderAnalyticsServiceTests {

	@Mock
	private OrderRepository repo;

	@InjectMocks
	private OrderAnalyticsService analyticsService;

	@Test
	void getKpis_success() {
		when(repo.fetchOrderCountAndRevenue())
				.thenReturn(new Object[]{5L, BigDecimal.valueOf(1000)});
		when(repo.countDeliveredOrders()).thenReturn(3L);

		OrderKpiResponse res = analyticsService.getKpis();

		assertEquals(5L, res.getTotalOrders());
		assertEquals(BigDecimal.valueOf(1000), res.getTotalRevenue());
		assertEquals(new BigDecimal("200.00"), res.getAvgOrderValue());
		assertEquals(3L, res.getDeliveredOrders());
	}

	@Test
	void getKpis_nullRepoResult() {
		when(repo.fetchOrderCountAndRevenue()).thenReturn(null);
		lenient().when(repo.countDeliveredOrders()).thenReturn(0L);
  
		OrderKpiResponse res = analyticsService.getKpis();

		assertEquals(0L, res.getTotalOrders());
		assertEquals(BigDecimal.ZERO, res.getTotalRevenue());
		assertEquals(BigDecimal.ZERO, res.getAvgOrderValue());
	}

	@Test
	void ordersByStatus_mapsCorrectly() {
		when(repo.countByStatus()).thenReturn(
			Collections.singletonList(new Object[]{OrderStatus.CREATED, 4L})
		);

		List<OrderStatusCountDto> res = analyticsService.ordersByStatus();

		assertEquals(1, res.size());
		assertEquals(OrderStatus.CREATED, res.get(0).getStatus());
		assertEquals(4L, res.get(0).getCount());
	}

	@Test
	void ordersByWarehouse_mapsCorrectly() {
		when(repo.countByWarehouse()).thenReturn(Collections.singletonList(new Object[]{1L, 10L}));

		List<WarehouseOrderCountDto> res = analyticsService.ordersByWarehouse();

		assertEquals(1, res.size());
		assertEquals(1L, res.get(0).getWarehouseId());
		assertEquals(10L, res.get(0).getCount());
	}

	@Test
	void revenueByWarehouse_mapsCorrectly() {
		when(repo.revenueByWarehouse())
			.thenReturn(Collections.singletonList(new Object[]{1L, BigDecimal.valueOf(5000)}));

		List<WarehouseRevenueDto> res = analyticsService.revenueByWarehouse();

		assertEquals(1, res.size());
		assertEquals(1L, res.get(0).getWarehouseId());
		assertEquals(BigDecimal.valueOf(5000), res.get(0).getRevenue());
	}

	@Test
	void ordersByState_mapsCorrectly() {
		when(repo.countByState()).thenReturn(Collections.singletonList(new Object[]{"DL", 20L}));

		List<OrdersByStateDto> res = analyticsService.ordersByState();

		assertEquals(1, res.size());
		assertEquals("DL", res.get(0).getState());
		assertEquals(20L, res.get(0).getCount());
	}
}

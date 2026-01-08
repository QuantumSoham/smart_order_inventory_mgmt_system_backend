package com.chubb.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.order.dto.request.CreateOrderRequest;
import com.chubb.order.dto.request.OrderItemRequest;
import com.chubb.order.dto.response.OrderResponse;
import com.chubb.order.dto.response.ProductResponse;
import com.chubb.order.entity.Order;
import com.chubb.order.entity.OrderItem;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.exception.BusinessException;
import com.chubb.order.feignclient.BillingClient;
import com.chubb.order.feignclient.InventoryClient;
import com.chubb.order.feignclient.NotificationClient;
import com.chubb.order.dto.response.OrderDetailedResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private BillingClient billingClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest createRequest;

    @BeforeEach
    void setup() {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(101L);
        item.setQuantity(2);

        createRequest = new CreateOrderRequest();
        createRequest.setUserId(1L);
        createRequest.setWarehouseId(10L);
        createRequest.setShippingName("John");
        createRequest.setShippingPhone("9999999999");
        createRequest.setShippingAddress("Street 1");
        createRequest.setCity("Bangalore");
        createRequest.setState("KA");
        createRequest.setPincode("560001");
        createRequest.setItems(List.of(item));
    }

    @Test
    void createOrder_productNotFound() {
        when(inventoryClient.getProduct(101L)).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> orderService.create(createRequest)
        );

        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void createOrder_success_approved() {
        ProductResponse product = new ProductResponse(
                101L, "Mouse", "Electronics", "cat", "img", BigDecimal.valueOf(10)
        );

        when(inventoryClient.getProduct(101L)).thenReturn(product);

        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(500L);
            if (o.getItems() != null) for (OrderItem it : o.getItems()) it.setOrder(o);
            return o;
        });

        OrderResponse res = orderService.create(createRequest);

        assertEquals(500L, res.getOrderId());
        assertEquals(OrderStatus.APPROVED, res.getStatus());
        assertEquals(new BigDecimal("20"), res.getTotalAmount());

        verify(inventoryClient).reserve(any(Map.class));
        verify(billingClient).createInvoice(any());
        verify(notificationClient).notifyOrderPlaced(any());
    }

    @Test
    void createOrder_billingFails_setsCreated() {
        ProductResponse product = new ProductResponse(
                101L, "Mouse", "Electronics", "cat", "img", BigDecimal.valueOf(5)
        );
        when(inventoryClient.getProduct(101L)).thenReturn(product);

        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(501L);
            if (o.getItems() != null) for (OrderItem it : o.getItems()) it.setOrder(o);
            return o;
        });

        doThrow(new RuntimeException("billing down")).when(billingClient).createInvoice(any());

        OrderResponse res = orderService.create(createRequest);

        assertEquals(501L, res.getOrderId());
        assertEquals(OrderStatus.CREATED, res.getStatus());

        verify(inventoryClient).reserve(any(Map.class));
        verify(billingClient).createInvoice(any());
    }

    @Test
    void getUserOrders_mapsProperly() {
        Order o = new Order();
        o.setId(10L);
        o.setUserId(1L);
        o.setWarehouseId(2L);
        o.setShippingName("John");
        o.setShippingPhone("999");
        o.setShippingAddress("Addr");
        o.setCity("City");
        o.setState("ST");
        o.setPincode("000");
        o.setStatus(OrderStatus.CREATED);
        o.setTotalAmount(BigDecimal.valueOf(15));
        o.setCreatedAt(LocalDateTime.now());

        when(orderRepo.findByUserId(1L)).thenReturn(List.of(o));

        List<OrderDetailedResponse> res = orderService.getUserOrders(1L);

        assertEquals(1, res.size());
        assertEquals(10L, res.get(0).getId());
        assertEquals(OrderStatus.CREATED, res.get(0).getStatus());
    }

    @Test
    void getAllOrders_and_getByStatus_and_getUserOrderStatus() {
        Order o = new Order();
        o.setId(20L);
        o.setStatus(OrderStatus.DELIVERED);
        o.setTotalAmount(BigDecimal.valueOf(100));
        o.setCreatedAt(LocalDateTime.now());

        when(orderRepo.findAll()).thenReturn(List.of(o));
        when(orderRepo.findByStatus(OrderStatus.DELIVERED)).thenReturn(List.of(o));
        when(orderRepo.findById(20L)).thenReturn(Optional.of(o));

        var all = orderService.getAllOrders();
        assertEquals(1, all.size());

        var byStatus = orderService.getByStatus(OrderStatus.DELIVERED);
        assertEquals(1, byStatus.size());

        var summary = orderService.getUserOrderStatus(20L);
        assertEquals(20L, summary.getOrderId());
        assertEquals(OrderStatus.DELIVERED, summary.getStatus());
    }

    @Test
    void createOrder_priceMissing() {
        ProductResponse product = new ProductResponse(
             101L,
             "Mouse",
             "Electronics",
             "test category",
             "image_url",
             null
        );

        when(inventoryClient.getProduct(101L)).thenReturn(product);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> orderService.create(createRequest)
        );

        assertTrue(ex.getMessage().contains("Price missing"));
    }

    @Test
    void cancelOrder_success() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancel(1L, 1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(inventoryClient).release(Map.of("orderId", 1L));
    }

    @Test
    void cancelOrder_notOwner_shouldFail() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(99L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> orderService.cancel(1L, 1L)
        );

        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    void cancelOrder_shipped_shouldFail() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> orderService.cancel(1L, 1L)
        );

        assertTrue(ex.getMessage().contains("Cannot cancel"));
    }

    @Test
    void updateStatus_ship_shouldCommitInventory() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.APPROVED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        var response = orderService.updateStatus(1L, OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, response.getStatus());
        verify(inventoryClient).commit(Map.of("orderId", 1L));
    }

    @Test
    void updateStatus_invalidApproval_shouldFail() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.APPROVED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> orderService.updateStatus(1L, OrderStatus.APPROVED)
        );

        assertTrue(ex.getMessage().contains("already been approved"));
    }
}
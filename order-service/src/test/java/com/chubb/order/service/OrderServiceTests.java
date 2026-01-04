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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.order.dto.request.*;
import com.chubb.order.dto.response.*;
import com.chubb.order.entity.*;
import com.chubb.order.exception.BusinessException;
import com.chubb.order.feignclient.BillingClient;
import com.chubb.order.feignclient.InventoryClient;
import com.chubb.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private BillingClient billingClient;

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

        OrderStatusResponse response =
                orderService.updateStatus(1L, OrderStatus.SHIPPED);

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

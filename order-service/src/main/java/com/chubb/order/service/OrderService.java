package com.chubb.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.*;

import com.chubb.order.dto.request.CreateOrderRequest;
import com.chubb.order.dto.request.OrderItemRequest;
import com.chubb.order.dto.response.OrderResponse;
import com.chubb.order.dto.response.OrderStatusResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.entity.Order;
import com.chubb.order.entity.OrderItem;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.exception.BusinessException;
import com.chubb.order.feignclient.BillingClient;
import com.chubb.order.feignclient.InventoryClient;
import com.chubb.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final InventoryClient inventoryClient;
    private final BillingClient billingClient;

    /* USER ACTIONS */

    public OrderResponse create(CreateOrderRequest req) {

        Order order = new Order();
        order.setUserId(req.getUserId());
        order.setWarehouseId(req.getWarehouseId());
        order.setStatus(OrderStatus.CREATED);

        order.setShippingName(req.getShippingName());
        order.setShippingPhone(req.getShippingPhone());
        order.setShippingAddress(req.getShippingAddress());
        order.setCity(req.getCity());
        order.setState(req.getState());
        order.setPincode(req.getPincode());

        BigDecimal total = BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest r : req.getItems()) {
            Map<String, Object> product =
                    inventoryClient.getProduct(r.getProductId());

            BigDecimal price =
                    new BigDecimal(product.get("price").toString());

            OrderItem oi = new OrderItem();
            oi.setProductId(r.getProductId());
            oi.setQuantity(r.getQuantity());
            oi.setPriceAtPurchase(price);
            oi.setOrder(order);

            total = total.add(price.multiply(
                    BigDecimal.valueOf(r.getQuantity())));

            items.add(oi);
        }

        order.setItems(items);
        order.setTotalAmount(total);

        orderRepo.save(order);

        inventoryClient.reserve(Map.of(
                "orderId", order.getId(),
                "warehouseId", order.getWarehouseId(),
                "items", req.getItems()
        ));

        order.setStatus(OrderStatus.APPROVED);

        billingClient.init(order.getId());

        return new OrderResponse(order.getId(), order.getStatus(), total);
    }

    public List<OrderSummaryResponse> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(o -> new OrderSummaryResponse(
                        o.getId(),
                        o.getStatus(),
                        o.getTotalAmount(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    public void cancel(Long orderId, Long userId) {
        Order order = find(orderId);

        if (!order.getUserId().equals(userId))
            throw new BusinessException("Unauthorized");

        if (order.getStatus() == OrderStatus.SHIPPED)
            throw new BusinessException("Cannot cancel shipped order");

        inventoryClient.release(Map.of("orderId", orderId));
        order.setStatus(OrderStatus.CANCELLED);
    }

    /* ADMIN / OPS ACTIONS */

    public List<OrderSummaryResponse> getByStatus(OrderStatus status) {
        return orderRepo.findByStatus(status).stream()
                .map(o -> new OrderSummaryResponse(
                        o.getId(),
                        o.getStatus(),
                        o.getTotalAmount(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    public OrderStatusResponse updateStatus(
            Long orderId, OrderStatus newStatus) {

        Order order = find(orderId);

        if (newStatus == OrderStatus.SHIPPED)
            inventoryClient.commit(Map.of("orderId", orderId));

        order.setStatus(newStatus);
        return new OrderStatusResponse(orderId, newStatus);
    }

    private Order find(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }
}

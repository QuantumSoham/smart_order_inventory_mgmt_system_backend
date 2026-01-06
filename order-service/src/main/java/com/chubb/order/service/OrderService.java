package com.chubb.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.*;

import com.chubb.order.dto.request.CreateInvoiceRequest;
import com.chubb.order.dto.request.CreateOrderRequest;
import com.chubb.order.dto.request.InvoiceItemRequest;
import com.chubb.order.dto.request.OrderItemRequest;
import com.chubb.order.dto.request.OrderPlacedEventRequest;
import com.chubb.order.dto.response.OrderDetailedResponse;
import com.chubb.order.dto.response.OrderResponse;
import com.chubb.order.dto.response.OrderStatusResponse;
import com.chubb.order.dto.response.OrderSummaryResponse;
import com.chubb.order.dto.response.ProductResponse;
import com.chubb.order.entity.Order;
import com.chubb.order.entity.OrderItem;
import com.chubb.order.entity.OrderStatus;
import com.chubb.order.exception.BusinessException;
import com.chubb.order.feignclient.BillingClient;
import com.chubb.order.feignclient.InventoryClient;
import com.chubb.order.feignclient.NotificationClient;
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
    private final NotificationClient notificationClient;

    

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
        List<OrderItem> orderItems = new ArrayList<>();

        
        for (OrderItemRequest r : req.getItems()) {

            ProductResponse product =
                    inventoryClient.getProduct(r.getProductId());

            if (product == null)
                throw new BusinessException(
                        "Product not found: " + r.getProductId()
                );

            if (product.getPrice() == null)
                throw new BusinessException(
                        "Price missing for product: " + product.getName()
                );

            BigDecimal price = product.getPrice();

            OrderItem oi = new OrderItem();
            oi.setProductId(product.getId());
            oi.setQuantity(r.getQuantity());
            oi.setPriceAtPurchase(price);
            oi.setOrder(order);

            total = total.add(
                    price.multiply(BigDecimal.valueOf(r.getQuantity()))
            );

            orderItems.add(oi);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        
        orderRepo.save(order);

        
        inventoryClient.reserve(Map.of(
                "orderId", order.getId(),
                "warehouseId", order.getWarehouseId(),
                "items", req.getItems()
        ));

        
        try {
            CreateInvoiceRequest invoiceReq = new CreateInvoiceRequest();
            invoiceReq.setOrderId(order.getId());
            invoiceReq.setUserId(order.getUserId());
            invoiceReq.setWarehouseId(order.getWarehouseId());
            invoiceReq.setTotalAmount(order.getTotalAmount());

            List<InvoiceItemRequest> invoiceItems = new ArrayList<>();

            for (OrderItem oi : order.getItems()) {

                ProductResponse product =
                        inventoryClient.getProduct(oi.getProductId());

                InvoiceItemRequest ir = new InvoiceItemRequest();
                ir.setProductId(product.getId());
                ir.setProductName(product.getName());
                ir.setCategory(product.getCategory());
                ir.setQuantity(oi.getQuantity());
                ir.setUnitPrice(oi.getPriceAtPurchase());

                invoiceItems.add(ir);
            }

            invoiceReq.setItems(invoiceItems);

            billingClient.createInvoice(invoiceReq);

            order.setStatus(OrderStatus.APPROVED);

        } catch (Exception e) {
            
            System.out.println("Billing skipped for order " + order.getId());
            order.setStatus(OrderStatus.CREATED);
        }

        try {
            notificationClient.notifyOrderPlaced(
                new OrderPlacedEventRequest(
                    order.getId(),
                    order.getUserId(),
                    "user@email.com", // later from User Service / JWT
                    order.getStatus().name(),
                    order.getTotalAmount().doubleValue()
                )
            );
        } catch (Exception e) {
            // 🔕 Notification failure should NOT break order
            System.out.println("Notification service unavailable");
        }

        
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }


    public List<OrderDetailedResponse> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(o -> new OrderDetailedResponse(
                		o.getId(),
                        o.getUserId(),
                        o.getWarehouseId(),
                        o.getShippingName(),
                        o.getShippingPhone(),
                        o.getShippingAddress(),
                        o.getCity(),
                        o.getState(),
                        o.getPincode(),
                        o.getStatus(),
                        o.getTotalAmount(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    public OrderSummaryResponse getUserOrderStatus(Long orderId)
    {
    	Order order= find(orderId);
    	return new OrderSummaryResponse(
    			order.getId(),
    			order.getStatus(),
    			order.getTotalAmount(),
    			order.getCreatedAt()
    			);
    }
    public void cancel(Long orderId, Long userId) {
        Order order = find(orderId);

        if (!order.getUserId().equals(userId))
            throw new BusinessException("Unauthorized");

        if (order.getStatus() == OrderStatus.SHIPPED)
            throw new BusinessException("Cannot cancel shipped order");

        if (order.getStatus() == OrderStatus.DELIVERED)
            throw new BusinessException("Cannot cancel delivered order");
        inventoryClient.release(Map.of("orderId", orderId));
        order.setStatus(OrderStatus.CANCELLED);
    }

    public List<OrderDetailedResponse> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(o -> new OrderDetailedResponse(
                        o.getId(),
                        o.getUserId(),
                        o.getWarehouseId(),
                        o.getShippingName(),
                        o.getShippingPhone(),
                        o.getShippingAddress(),
                        o.getCity(),
                        o.getState(),
                        o.getPincode(),
                        o.getStatus(),
                        o.getTotalAmount(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    // admin operations

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

        if (newStatus==OrderStatus.APPROVED && order.getStatus() != OrderStatus.CREATED)
            throw new BusinessException("Order has already been approved.");
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

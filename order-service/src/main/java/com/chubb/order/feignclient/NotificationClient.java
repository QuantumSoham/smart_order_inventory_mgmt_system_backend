package com.chubb.order.feignclient;

import com.chubb.order.dto.request.OrderPlacedEventRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "notification-service",
    url = "http://localhost:9086" // Kafka demo app port
)
public interface NotificationClient {

    @PostMapping("/notifications/order-placed")
    void notifyOrderPlaced(@RequestBody OrderPlacedEventRequest req);
}

package com.example.demo.controller;

import com.example.demo.dto.OrderPlacedEvent;
import com.example.demo.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class OrderNotificationController {

    private final MessageProducer producer;

    @PostMapping("/order-placed")
    public String orderPlaced(@RequestBody OrderPlacedEvent event) {

        producer.send(event);

        return "Order notification published to Kafka";
    }
}


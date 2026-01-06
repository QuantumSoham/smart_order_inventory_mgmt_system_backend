package com.example.demo.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderPlacedEvent;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private static final String TOPIC = "order-events";

    public void send(OrderPlacedEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("Produced event: " + event.getOrderId());
    }
}

//@Service
//@RequiredArgsConstructor
//public class MessageProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private static final String TOPIC = "simple-topic";
//
//    public void send(String message) {
//        kafkaTemplate.send(TOPIC, message);
//        System.out.println("Produced: " + message);
//    }
//}

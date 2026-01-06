package com.example.demo.consumer;

import com.example.demo.dto.OrderPlacedEvent;
import com.example.demo.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "order-events", groupId = "mail-group")
    public void consume(OrderPlacedEvent event) {

        System.out.println("Consumed event for order " + event.getOrderId());

        try {
            mailService.sendMail(
                "quantumsc03@gmail.com",
                "Order Update: " + event.getStatus(),
                """
                Your order has been updated.

                Order ID: %d
                Status: %s
                Amount: %s
                """.formatted(
                    event.getOrderId(),
                    event.getStatus(),
                    event.getTotalAmount()
                )
            );

            System.out.println("Mail sent to " + "quantumsc03@gmail.com");

        } catch (Exception e) {
            System.out.println("Mail service unavailable. Will retry later.");
        }
    }
}

//@Service
//@RequiredArgsConstructor
//public class MessageConsumer {
//
//    private final MailService mailService;
//
//    @KafkaListener(topics = "simple-topic")
//    public void consume(String message) {
//
//        System.out.println("Consumed from Kafka: " + message);
//
//        // crude example
//        String clientEmail = "quantumsc03@gmail.com";
//        try
//        {
//        mailService.sendMail(
//            clientEmail,
//            "Order Update",
//            "Kafka says: " + message
//        );
//        System.out.println("Mail Sent!!");
//        }
//        catch(Exception e)
//        {
//        	System.out.println("Cannot connect to mail service for now!");
//        }
//    }
//}

package com.example.demo.controller;

import com.example.demo.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

//    private final MessageProducer producer;
//
//    @GetMapping("/send")
//    public String send(@RequestParam String msg) {
//        producer.send(msg);
//        return "Sent: " + msg;
//    }
}

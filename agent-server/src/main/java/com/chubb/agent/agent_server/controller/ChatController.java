package com.chubb.agent.agent_server.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  @Autowired
  public ChatController(ChatClient.Builder builder) {
	  System.out.println(">>> ChatController loaded <<<");
	  this.chatClient = builder.build();
  }

  @GetMapping("/ai/generate")
  public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
	  System.out.println("Endpoint hit");
     String response = chatClient.prompt().user(message).call().content();
     return Map.of("generation", response);
  }

  @GetMapping("/ai/generateStream")
  public Flux<String> generateStream(@RequestParam(value = "message", 
        defaultValue = "Tell me a joke") String message) {
      return chatClient.prompt().user(message).stream().content();
  }
}
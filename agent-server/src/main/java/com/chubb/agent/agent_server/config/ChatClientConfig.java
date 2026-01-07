//package com.chubb.agent.agent_server.config;
//
//package com.chubb.agent.agent_server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.model.openai.OpenAiChatModel;
//
//@Configuration
//public class ChatClientConfig {
//
//    @Bean
//    public ChatClient chatClient() {
//        // Configure the model you want to use
//        ChatModel model = OpenAiChatModel.builder()
//                .modelId("gpt-4")   // or "gpt-3.5-turbo"
//                .apiKey(System.getenv("OPENAI_API_KEY")) // Make sure your key is set
//                .build();
//
//        return ChatClient.builder(model).build();
//    }
//}

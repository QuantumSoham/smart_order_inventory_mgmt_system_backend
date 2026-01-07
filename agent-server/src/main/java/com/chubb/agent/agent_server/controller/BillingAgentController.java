package com.chubb.agent.agent_server.controller;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chubb.agent.agent_server.agent.function.CategorySalesFunction;
import com.chubb.agent.agent_server.agent.function.RevenueReportFunction;
import com.chubb.agent.agent_server.agent.function.TopProductsFunction;
import com.chubb.agent.agent_server.agent.function.WarehouseSalesFunction;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class BillingAgentController {

    private final ChatClient chatClient;

    private final RevenueReportFunction revenueReportFunction;
    private final CategorySalesFunction categorySalesFunction;
    private final WarehouseSalesFunction warehouseSalesFunction;
    private final TopProductsFunction topProductsFunction;

    public BillingAgentController(
            ChatClient.Builder builder,
            RevenueReportFunction revenueReportFunction,
            CategorySalesFunction categorySalesFunction,
            WarehouseSalesFunction warehouseSalesFunction,
            TopProductsFunction topProductsFunction
    ) {
        this.chatClient = builder.build();
        this.revenueReportFunction = revenueReportFunction;
        this.categorySalesFunction = categorySalesFunction;
        this.warehouseSalesFunction = warehouseSalesFunction;
        this.topProductsFunction = topProductsFunction;

        System.out.println(">>> BillingAgentController loaded <<<");
    }

    @PostMapping("/ai/billing")
    public String askBillingAgent(@RequestBody String question) {

        return chatClient.prompt()
        		.system(
        				"""
                        You are a billing analytics assistant.
                        Use tools when data is required.
                        Do not hallucinate numbers.
                        You have been given 4 tools use them.You are allowed to say what can you help users with
                        If the user asks any question unnrelated to billing service , or analytics , refuse to answer.
                        All currency numbers are in Indian Rupee
                        Give response in plain text with no formatting or special formatting characters.
                        """)
                .user(question)
                .tools(
                        revenueReportFunction,
                        categorySalesFunction,
                        warehouseSalesFunction,
                        topProductsFunction
                )
                .call()
                .content();
    }
}


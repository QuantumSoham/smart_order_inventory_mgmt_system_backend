package com.chubb.agent.agent_server.agent.function;



import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

import com.chubb.agent.agent_server.feignclient.BillingReportFeignClient;




import org.springframework.ai.tool.annotation.Tool;
import com.chubb.agent.agent_server.agent.dto.CategorySalesAgentResponse;

import org.springframework.ai.tool.annotation.Tool;

@Component
@RequiredArgsConstructor
public class CategorySalesFunction {

    private final BillingReportFeignClient billingClient;

    @Tool(
        name = "get_category_sales",
        description = "Get total sales grouped by product category"
    )
    public CategorySalesAgentResponse getCategorySales() {
        return new CategorySalesAgentResponse(
                billingClient.salesByCategory()
        );
    }
}


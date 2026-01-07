package com.chubb.agent.agent_server.agent.function;


import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import com.chubb.agent.agent_server.agent.dto.TopProductsAgentResponse;
import com.chubb.agent.agent_server.feignclient.BillingReportFeignClient;

@Component
@RequiredArgsConstructor
public class TopProductsFunction {

    private final BillingReportFeignClient billingClient;

    @Tool(
        name = "get_top_products",
        description = "Get top selling products ranked by revenue"
    )
    public TopProductsAgentResponse getTopProducts() {
        return new TopProductsAgentResponse(
                billingClient.topProducts()
        );
    }
}

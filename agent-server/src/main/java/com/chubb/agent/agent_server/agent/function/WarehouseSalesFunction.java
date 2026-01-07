package com.chubb.agent.agent_server.agent.function;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.chubb.agent.agent_server.agent.dto.WarehouseSalesAgentResponse;
import com.chubb.agent.agent_server.feignclient.BillingReportFeignClient;

@Component
@RequiredArgsConstructor
public class WarehouseSalesFunction {

    private final BillingReportFeignClient billingClient;

    @Tool(
        name = "get_warehouse_sales",
        description = "Get total sales grouped by warehouse"
    )
    public WarehouseSalesAgentResponse getWarehouseSales() {
        return new WarehouseSalesAgentResponse(
                billingClient.salesByWarehouse()
        );
    }
}

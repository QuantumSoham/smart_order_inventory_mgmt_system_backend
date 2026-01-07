package com.chubb.agent.agent_server.agent.function;

import java.time.LocalDate;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Description;

import lombok.RequiredArgsConstructor;

import com.chubb.agent.agent_server.agent.dto.*;
import com.chubb.agent.agent_server.feignclient.BillingReportFeignClient;
import com.chubb.agent.agent_server.dto.request.RevenueReportRequest;

@Component
public class RevenueReportFunction {

    private final BillingReportFeignClient billingClient;

    public RevenueReportFunction(BillingReportFeignClient billingClient) {
        this.billingClient = billingClient;
    }

    @Tool(
        name = "get_revenue_report",
        description = "Get revenue report between two dates"
    )
    public RevenueReportAgentResponse getRevenue(
            String from,
            String to
    ) {
        return new RevenueReportAgentResponse(
                billingClient.revenueByDate(
                        LocalDate.parse(from),
                        LocalDate.parse(to)
                )
        );
    }
}


package com.chubb.agent.agent_server.agent.dto;

import java.util.List;
import com.chubb.agent.agent_server.dto.response.CategorySalesDTO;

public record CategorySalesAgentResponse(
        List<CategorySalesDTO> data
) {}

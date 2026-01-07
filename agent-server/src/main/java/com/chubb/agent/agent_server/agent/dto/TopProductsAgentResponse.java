package com.chubb.agent.agent_server.agent.dto;

import java.util.List;
import com.chubb.agent.agent_server.dto.response.TopProductDTO;

public record TopProductsAgentResponse(
        List<TopProductDTO> data
) {}

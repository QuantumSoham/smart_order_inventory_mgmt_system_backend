package com.chubb.agent.agent_server.agent.dto;

import java.util.List;
import com.chubb.agent.agent_server.dto.response.WarehouseSalesDTO;

public record WarehouseSalesAgentResponse(
        List<WarehouseSalesDTO> data
) {}

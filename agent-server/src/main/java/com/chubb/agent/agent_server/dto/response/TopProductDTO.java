package com.chubb.agent.agent_server.dto.response;

import java.math.BigDecimal;

public record TopProductDTO(
        String productName,
        BigDecimal revenue
) {}

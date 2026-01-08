package com.chubb.inventory.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTests {

    @Test
    void handle_businessException_returnsBadRequestBody() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();

        BusinessException ex = new BusinessException("Test failure");

        ResponseEntity<?> resp = h.handle(ex);

        assertEquals(400, resp.getStatusCode().value());
        assertTrue(resp.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertEquals("Test failure", body.get("error"));
    }
}

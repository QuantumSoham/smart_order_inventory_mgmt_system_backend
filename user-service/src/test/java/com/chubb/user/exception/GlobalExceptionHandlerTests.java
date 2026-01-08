package com.chubb.user.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTests {

    @Test
    void handleBusinessException_returns401WithMessage() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        BusinessException ex = new BusinessException("bad creds");

        ResponseEntity<?> resp = h.handleBusinessException(ex);
        assertEquals(401, resp.getStatusCode().value());
        assertTrue(resp.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertEquals("bad creds", body.get("message"));
        assertEquals(401, body.get("status"));
    }

    @Test
    void handleGenericException_returns500() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        Exception ex = new RuntimeException("boom");
        ResponseEntity<?> resp = h.handleGenericException(ex);
        assertEquals(500, resp.getStatusCode().value());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertEquals(500, body.get("status"));
    }
}

package com.chubb.user.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtUtilTests {

    @Test
    void generateToken_nonNull_and_structure() {
        JwtUtil util = new JwtUtil();
        String token = util.generateToken(1L, "a@b.com", "USER");
        assertNotNull(token);
        // JWT has three parts separated by dots
        assertTrue(token.split("\\.").length >= 3);
    }
}

package br.com.blackbelt.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void deveGerarToken() {

        JwtService jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "BlackBelt-Dev-JWT-Secret-Key-2026-0123456789"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                3600000L
        );

        String token = jwtService.gerarToken("admin");

        assertNotNull(token);
        assertFalse(token.isBlank());

        System.out.println("JWT GERADO:");
        System.out.println(token);
    }
}
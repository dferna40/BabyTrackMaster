package com.babytrackmaster.api_gastos.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
        String secret = Base64.getEncoder().encodeToString("testsecretkeytestsecretkeytest12".getBytes());
        ReflectionTestUtils.setField(jwtService, "secretB64", secret);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);
    }

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
        SecurityContextHolder.clearContext();
    }

    @Test
    void resolveUserIdFromSecurityContext() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(77L, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertEquals(77L, jwtService.resolveUserId());
    }

    @Test
    void resolveUserIdFromValidToken() {
        String token = jwtService.generarToken("123");
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));
        assertEquals(123L, jwtService.resolveUserId());
    }

    @Test
    void resolveUserIdReturnsNullWhenUnauthorized() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));
        assertNull(jwtService.resolveUserId());
    }
}

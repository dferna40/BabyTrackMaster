package com.babytrackmaster.api_citas.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.babytrackmaster.api_citas.dto.CitaResponseDTO;
import com.babytrackmaster.api_citas.security.JwtService;
import com.babytrackmaster.api_citas.service.CitaService;

@WebMvcTest(CitaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService service;

    @MockBean
    private JwtService jwtService;

    @Test
    void proximasDevuelveOk() throws Exception {
        CitaResponseDTO cita = CitaResponseDTO.builder()
                .id(1L)
                .motivo("Chequeo")
                .build();
        when(jwtService.resolveUserId()).thenReturn(1L);
        when(service.proximas(1L, 5)).thenReturn(List.of(cita));

        mockMvc.perform(get("/api/v1/citas/proximas").param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].motivo").value("Chequeo"));

        verify(service).proximas(eq(1L), eq(5));
    }
}

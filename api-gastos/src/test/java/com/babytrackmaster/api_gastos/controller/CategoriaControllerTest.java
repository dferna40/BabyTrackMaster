package com.babytrackmaster.api_gastos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.security.JwtService;
import com.babytrackmaster.api_gastos.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private JwtService jwtService;

    @Test
    void crearRetornaCreated() throws Exception {
        CategoriaResponse resp = new CategoriaResponse();
        resp.setId(1L);
        resp.setNombre("Ropa");
        Mockito.when(jwtService.resolveUserId()).thenReturn(1L);
        Mockito.when(categoriaService.crear(Mockito.any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Ropa\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nombre").value("Ropa"));
    }

    @Test
    void listarRetornaListaOrdenada() throws Exception {
        CategoriaResponse c1 = new CategoriaResponse();
        c1.setId(1L);
        c1.setNombre("Agua");
        CategoriaResponse c2 = new CategoriaResponse();
        c2.setId(2L);
        c2.setNombre("Comida");
        Mockito.when(jwtService.resolveUserId()).thenReturn(1L);
        Mockito.when(categoriaService.listar()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/categorias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Agua"))
            .andExpect(jsonPath("$[1].nombre").value("Comida"));
    }

    @Test
    void listarSinAutenticacionRetorna401() throws Exception {
        Mockito.when(jwtService.resolveUserId()).thenReturn(null);

        mockMvc.perform(get("/api/v1/categorias"))
            .andExpect(status().isUnauthorized());
    }
}

package com.babytrackmaster.api_alimentacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.babytrackmaster.api_alimentacion.controller.AlimentacionController;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.service.AlimentacionService;

@WebMvcTest(AlimentacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlimentacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlimentacionService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void listarDevuelveOk() throws Exception {
        when(service.listar(1L,2L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/alimentacion/usuario/1/bebe/2"))
               .andExpect(status().isOk());
    }

    @Test
    void tiposLactanciaDevuelveOk() throws Exception {
        when(service.listarTiposLactancia()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/alimentacion/tipos-lactancia"))
               .andExpect(status().isOk());
    }

    @Test
    void tiposAlimentacionDevuelveOk() throws Exception {
        when(service.listarTiposAlimentacion()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/alimentacion/tipos-alimentacion"))
               .andExpect(status().isOk());
    }

    @Test
    void tiposBiberonDevuelveOk() throws Exception {
        when(service.listarTiposBiberon()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/alimentacion/tipos-biberon"))
               .andExpect(status().isOk());
    }

    @Test
    void tiposAlimentacionSolidoDevuelveOk() throws Exception {
        when(service.listarTiposAlimentacionSolido()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/alimentacion/tipos-alimentacion-solido"))
               .andExpect(status().isOk());
    }

    @Test
    void crearDevuelveCreated() throws Exception {
        AlimentacionRequest req = new AlimentacionRequest();
        TipoAlimentacion tipo = new TipoAlimentacion();
        tipo.setId(2L);
        tipo.setNombre("Biberón");
        req.setTipoAlimentacion(tipo);
        req.setFechaHora(LocalDateTime.now());
        req.setCantidadMl(100);
        AlimentacionResponse resp = new AlimentacionResponse();
        resp.setId(1L);
        when(service.crear(any(Long.class), any(Long.class), any(AlimentacionRequest.class))).thenReturn(resp);

        String json = mapper.writeValueAsString(req);

        mockMvc.perform(post("/api/v1/alimentacion/usuario/1/bebe/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crearSinFechaHoraDevuelveCreated() throws Exception {
        AlimentacionRequest req = new AlimentacionRequest();
        TipoAlimentacion tipo = new TipoAlimentacion();
        tipo.setId(2L);
        tipo.setNombre("Biberón");
        req.setTipoAlimentacion(tipo);
        req.setCantidadMl(100);
        AlimentacionResponse resp = new AlimentacionResponse();
        resp.setId(1L);
        resp.setFechaHora(LocalDateTime.now());
        when(service.crear(any(Long.class), any(Long.class), any(AlimentacionRequest.class))).thenReturn(resp);

        String json = mapper.writeValueAsString(req);

        mockMvc.perform(post("/api/v1/alimentacion/usuario/1/bebe/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }
}

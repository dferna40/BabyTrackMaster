package com.babytrackmaster.api_citas.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class EstadoCitaDtoSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializaResponse() throws Exception {
        EstadoCitaResponseDTO dto = EstadoCitaResponseDTO.builder()
                .id(1L)
                .nombre("Programada")
                .build();
        String json = mapper.writeValueAsString(dto);
        assertThat(json).contains("\"id\":1", "\"nombre\":\"Programada\"");
    }
}


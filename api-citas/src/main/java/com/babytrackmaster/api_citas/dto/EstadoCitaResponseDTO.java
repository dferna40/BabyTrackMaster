package com.babytrackmaster.api_citas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EstadoCitaResponseDTO {
    private Long id;
    private String nombre;
}

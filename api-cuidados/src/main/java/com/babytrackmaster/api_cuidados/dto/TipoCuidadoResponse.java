package com.babytrackmaster.api_cuidados.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "TipoCuidadoResponse", description = "Representación de un tipo de cuidado")
public class TipoCuidadoResponse {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Alimentación")
    private String nombre;

    private Date createdAt;
    private Date updatedAt;
}

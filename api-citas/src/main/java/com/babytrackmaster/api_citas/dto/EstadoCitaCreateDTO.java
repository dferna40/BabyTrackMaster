package com.babytrackmaster.api_citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoCitaCreateDTO {
    @Schema(example = "Pendiente")
    @NotBlank
    @Size(max = 20)
    private String nombre;
}

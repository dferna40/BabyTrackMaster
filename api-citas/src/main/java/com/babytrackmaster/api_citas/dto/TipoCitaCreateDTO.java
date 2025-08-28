package com.babytrackmaster.api_citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCitaCreateDTO {

    @Schema(example = "Pediatría")
    @NotBlank
    @Size(max = 100)
    private String nombre;
}

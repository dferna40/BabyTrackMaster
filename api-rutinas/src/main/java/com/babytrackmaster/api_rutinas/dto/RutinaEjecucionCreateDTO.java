package com.babytrackmaster.api_rutinas.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RutinaEjecucionCreateDTO {

    @NotBlank
    private String fechaHora; // ISO-8601: "2025-08-27T08:30:00"

    @Size(max=20)
    private String estado;

    @Size(max=300)
    private String nota;
}
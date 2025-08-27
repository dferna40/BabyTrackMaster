package com.babytrackmaster.api_rutinas.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RutinaCreateDTO {

    @NotBlank
    @Size(max=120)
    private String nombre;

    @Size(max=500)
    private String descripcion;

    @NotBlank
    private String horaProgramada; // "HH:mm"

    @NotBlank
    private String diasSemana; // "L,M,X,J,V" o "1,3,5"

    private Boolean activa;
}

package com.babytrackmaster.api_hitos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HitoRequest {

    @NotNull
    private Long bebeId;

    @NotNull
    @Size(min = 1, max = 150)
    private String titulo;

    @NotNull
    private LocalDate fecha;

    @Size(max = 1000)
    private String descripcion;

    @Size(max = 500)
    private String imagenUrl;
}

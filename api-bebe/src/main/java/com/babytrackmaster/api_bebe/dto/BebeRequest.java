package com.babytrackmaster.api_bebe.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BebeRequest {
    @NotBlank
    private String nombre;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotBlank
    private String sexo;
}

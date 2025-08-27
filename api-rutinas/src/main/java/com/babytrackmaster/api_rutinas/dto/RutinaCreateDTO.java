package com.babytrackmaster.api_rutinas.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Data
@Schema(name = "RutinaCreateDTO", description = "Datos para registrar una rutina")
public class RutinaCreateDTO {

    @NotBlank
    @Size(max=120)
    private String nombre;

    @Size(max=500)
    private String descripcion;

    @NotBlank
    @Pattern(regexp="^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$", message="horaProgramada debe tener formato HH:mm o HH:mm:ss")
    private String horaProgramada; // "HH:mm"

    @NotBlank
    @Pattern(regexp="^(L|M|X|J|V|S|D)(,(L|M|X|J|V|S|D))*$", message="diasSemana debe ser una lista como L,M,X,J,V,S,D")
    private String diasSemana; // "L,M,X,J,V" o "1,3,5"

    private Boolean activa;
}

package com.babytrackmaster.api_cuidados.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "CuidadoRequest", description = "Datos para crear/actualizar un cuidado")
public class CuidadoRequest {
    @Schema(example = "1", description = "ID del bebé al que pertenece el cuidado")
    @NotNull private Long bebeId;
    @Schema(example = "1", description = "ID del tipo de cuidado")
    @NotNull private Long tipoId;
    @NotNull private Date inicio;

    private Date fin;
    private String duracion;
    private Integer cantidadMl;
    @Schema(example = "1", description = "ID del tipo de pañal")
    private Long tipoPanalId;
    @Schema(example = "2", description = "Cantidad de pañales")
    private Integer cantidadPanal;
    private String medicamento;
    private String dosis;
    private String observaciones;
}

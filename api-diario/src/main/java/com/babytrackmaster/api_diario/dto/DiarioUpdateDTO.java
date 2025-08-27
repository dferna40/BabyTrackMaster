package com.babytrackmaster.api_diario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DiarioUpdateDTO {

    @Schema(example = "Primera sonrisa actualizada")
    @NotBlank @Size(max=120)
    private String titulo;

    @Schema(example = "Detalle actualizado")
    private String contenido;

    @Schema(example = "contento")
    private String estadoAnimo;

    @Schema(example = "sueño,hitó,lactancia")
    private String etiquetas;

    @Schema(example = "http://.../foto1.jpg,http://.../foto2.jpg")
    private String fotosUrl;
}
package com.babytrackmaster.api_diario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DiarioCreateDTO {

    @Schema(example = "2025-08-27")
    @NotNull private String fecha;        // ISO yyyy-MM-dd

    @Schema(example = "09:30")
    @NotNull private String hora;         // HH:mm

    @Schema(example = "Primera sonrisa")
    @NotBlank @Size(max=120)
    private String titulo;

    @Schema(example = "Hoy el bebé ha sonreído después de la siesta.")
    private String contenido;

    @Schema(example = "feliz")
    private String estadoAnimo;

    @Schema(example = "sueño,hitó,lactancia")
    private String etiquetas;

    @Schema(example = "http://.../foto1.jpg,http://.../foto2.jpg")
    private String fotosUrl;
}

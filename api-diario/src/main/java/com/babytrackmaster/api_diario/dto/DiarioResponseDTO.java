package com.babytrackmaster.api_diario.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DiarioResponseDTO {
    private Long id;
    private Long usuarioId;
    private LocalDate fecha;
    private LocalTime hora;
    private String titulo;
    private String contenido;
    private String estadoAnimo;
    private String etiquetas;
    private String fotosUrl;
}

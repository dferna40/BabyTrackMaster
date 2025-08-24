package com.babytrackmaster.api_gastos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoResponse {
    private Long id;
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal cantidad;
    private LocalDate fecha;
    private String descripcion;
    private Long bebeId;
}

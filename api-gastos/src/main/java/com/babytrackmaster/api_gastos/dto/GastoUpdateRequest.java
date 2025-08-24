package com.babytrackmaster.api_gastos.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoUpdateRequest {
    @NotNull
    private Long categoriaId;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal cantidad;

    @NotNull
    private LocalDate fecha;

    @Size(max = 255)
    private String descripcion;

    private Long bebeId;
}
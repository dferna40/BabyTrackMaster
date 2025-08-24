package com.babytrackmaster.api_gastos.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoCreateRequest {
    @NotNull
    private Long categoriaId;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal cantidad;

    @NotNull
    private LocalDate fecha;

    @Size(max = 255)
    private String descripcion;

    // Opcional si manejas multi-bebé
    private Long bebeId;
}

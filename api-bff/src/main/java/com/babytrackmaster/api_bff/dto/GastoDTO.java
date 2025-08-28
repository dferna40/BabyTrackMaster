package com.babytrackmaster.api_bff.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoDTO {
  private Long id;
  private Long categoriaId;
  private BigDecimal cantidad;
  private LocalDate fecha;
  private String descripcion;
  private Long bebeId;
}

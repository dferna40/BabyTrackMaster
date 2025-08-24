package com.babytrackmaster.api_gastos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResumenMensualResponse {
    @Data
    public static class ItemCategoria {
        private Long categoriaId;
        private String categoriaNombre;
        private BigDecimal total;
    }

    private int anio;
    private int mes; // 1-12
    private BigDecimal totalMes;
    private List<ItemCategoria> totalesPorCategoria;
}
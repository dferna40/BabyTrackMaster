package com.babytrackmaster.api_gastos.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ResumenMensualResponse", description = "Resumen mensual de gastos por categoría")
public class ResumenMensualResponse {
    @Data
    @Schema(name = "ItemCategoria", description = "Total acumulado por categoría en el mes")
    public static class ItemCategoria {
    	@Schema(example = "2")
        private Long categoriaId;
    	@Schema(example = "Pañales")
        private String categoriaNombre;
    	@Schema(example = "125.40", description = "Total gastado en la categoría")
        private BigDecimal total;
    }

    @Schema(example = "2025", description = "Año del resumen")
    private int anio;
    @Schema(example = "8", description = "Mes del resumen (1-12)")
    private int mes; // 1-12
    @Schema(example = "245.90", description = "Total gastado en el mes")
    private BigDecimal totalMes;
    @Schema(description = "Totales agrupados por categoría",
            example = "[{\"categoriaId\":2,\"categoriaNombre\":\"Pañales\",\"total\":125.40},{\"categoriaId\":5,\"categoriaNombre\":\"Ropa\",\"total\":120.50}]")
    private List<ItemCategoria> totalesPorCategoria;
}
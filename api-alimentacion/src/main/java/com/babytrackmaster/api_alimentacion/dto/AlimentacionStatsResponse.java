package com.babytrackmaster.api_alimentacion.dto;

import java.util.List;

import lombok.Data;

@Data
public class AlimentacionStatsResponse {
    private long lactanciaDia;
    private long biberonDia;
    private long solidosDia;
    private long lactanciaSemana;
    private long biberonSemana;
    private long solidosSemana;
    private double porcentajeLactancia;
    private double porcentajeBiberon;
    /**
     * Total de registros por día de la semana (lunes a domingo).
     */
    private List<Long> weekly;
}

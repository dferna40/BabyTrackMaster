package com.babytrackmaster.api_citas.dto;

import com.babytrackmaster.api_citas.enums.EstadoCita;

import jakarta.validation.constraints.NotNull;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class EstadoCitaUpdateDTO {
    @NotNull
    private EstadoCita estado;
}
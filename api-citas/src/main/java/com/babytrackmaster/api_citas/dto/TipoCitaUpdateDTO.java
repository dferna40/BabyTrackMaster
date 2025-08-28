package com.babytrackmaster.api_citas.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCitaUpdateDTO {

    @Size(max = 100)
    private String nombre;
}

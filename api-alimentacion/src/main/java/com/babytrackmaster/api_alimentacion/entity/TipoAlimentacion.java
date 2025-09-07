package com.babytrackmaster.api_alimentacion.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoAlimentacion {
    LACTANCIA,
    BIBERON,
    SOLIDOS;

    /**
     * Permite la deserialización case-insensitive del enum.
     *
     * @param value cadena con el tipo de alimentación
     * @return enum correspondiente
     */
    @JsonCreator
    public static TipoAlimentacion from(String value) {
        return TipoAlimentacion.valueOf(value.toUpperCase());
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

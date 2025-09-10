package com.babytrackmaster.api_alimentacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Catálogo para los alimentos sólidos ofrecidos al bebé.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tipo_alimentacion_solidos")
public class TipoAlimentacionSolido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}


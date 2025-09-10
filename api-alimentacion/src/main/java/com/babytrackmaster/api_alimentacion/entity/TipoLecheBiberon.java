package com.babytrackmaster.api_alimentacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Catálogo de tipos de leche utilizados en el biberón.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tipo_leche_biberon")
public class TipoLecheBiberon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}


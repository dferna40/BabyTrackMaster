package com.babytrackmaster.api_rutinas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rutina_ejecucion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RutinaEjecucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="rutina_id")
    private Rutina rutina;

    @Column(name="fecha_hora", nullable=false)
    private LocalDateTime fechaHora;

    @Column(length=20)
    private String estado; // OK, FALLIDA, OMITIDA

    @Column(length=300)
    private String nota;
}
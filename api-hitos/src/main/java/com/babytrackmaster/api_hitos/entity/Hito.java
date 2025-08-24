package com.babytrackmaster.api_hitos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "hitos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Hito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name="bebe_id", nullable = false)
    private Long bebeId;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 1000)
    private String descripcion;

    @Column(name="imagen_url", length = 500)
    private String imagenUrl;
}
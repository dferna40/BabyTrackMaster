package com.babytrackmaster.api_cuidados.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cuidados")
public class Cuidado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="bebe_id", nullable=false)
    private Long bebeId;

    @Column(name="usuario_id", nullable=false)
    private Long usuarioId;

    @Column(nullable=false, length=30)
    private String tipo; // Enum en DTO, string en DB

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date inicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;

    @Column(name="cantidad_ml")
    private Integer cantidadMl;

    @Column(length=10)
    private String pecho; // IZQ/DER

    @Column(name="tipo_panal", length=20)
    private String tipoPanal; // PIPI/CACA/MIXTO

    @Column(length=120)
    private String medicamento;

    @Column(length=60)
    private String dosis;

    @Column(length=500)
    private String observaciones;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at", nullable=false)
    private Date updatedAt;
}
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

    @ManyToOne
    @JoinColumn(name = "tipo", nullable = false)
    private TipoCuidado tipo;

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

    @Column(nullable = false)
    private Boolean eliminado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at", nullable=false)
    private Date updatedAt;
}
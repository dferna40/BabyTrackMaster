package com.babytrackmaster.api_alimentacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "alimentacion")
public class Alimentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "bebe_id", nullable = false)
    private Long bebeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAlimentacion tipo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_hora", nullable = false)
    private Date fechaHora;

    // Lactancia
    private String lado; // IZQ/DER
    private Integer duracionMin;

    @ManyToOne
    @JoinColumn(name = "tipo_lactancia_id")
    private TipoLactancia tipoLactancia;

    // Biberon
    private String tipoLeche;
    private Integer cantidadMl;
    private Integer cantidadLecheFormula;

    // Solidos
    private String alimento;
    private String cantidad;
    private Integer cantidadOtrosAlimentos;
    private String observaciones;

    @Column(nullable = false)
    private Boolean eliminado = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}

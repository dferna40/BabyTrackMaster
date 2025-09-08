package com.babytrackmaster.api_crecimiento.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "crecimientos")
public class Crecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bebe_id", nullable = false)
    private Long bebeId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToOne
    @JoinColumn(name = "tipo", nullable = false)
    private TipoCrecimiento tipo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private Double valor;

    @Column(length = 20)
    private String unidad;

    @Column(length = 500)
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

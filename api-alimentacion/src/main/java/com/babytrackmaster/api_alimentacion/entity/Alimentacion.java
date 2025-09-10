package com.babytrackmaster.api_alimentacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (fechaHora == null) {
            fechaHora = now;
        }
        createdAt = now;
        //updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

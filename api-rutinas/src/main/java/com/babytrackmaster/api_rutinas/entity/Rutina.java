package com.babytrackmaster.api_rutinas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "rutina")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usuario_id", nullable=false)
    private Long usuarioId;

    @Column(nullable=false, length=120)
    private String nombre;

    @Column(length=500)
    private String descripcion;

    @Column(name="hora_programada", nullable=false)
    private LocalTime horaProgramada;

    @Column(name="dias_semana", nullable=false, length=20)
    private String diasSemana; // Ej: "L,M,X,J,V" o "1,3,5"

    @Column(nullable=false)
    private Boolean activa;

    @Column(nullable=false)
    private Boolean eliminado = Boolean.FALSE;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.activa == null) this.activa = Boolean.TRUE;
        if (this.eliminado == null) this.eliminado = Boolean.FALSE;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

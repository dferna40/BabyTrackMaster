package com.babytrackmaster.api_hitos.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "hitos")
@Data
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
    
    @Column(name="creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name="actualizado_en")
    private LocalDateTime actualizadoEn;
    
    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}
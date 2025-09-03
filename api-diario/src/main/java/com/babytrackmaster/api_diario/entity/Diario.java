package com.babytrackmaster.api_diario.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@Table(name = "diario")
@Data
public class Diario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usuario_id", nullable=false)
    private Long usuarioId;

    @Column(name="bebe_id", nullable=false)
    private Long bebeId;

    @Column(nullable=false)
    private LocalDate fecha;

    @Column(nullable=false)
    private LocalTime hora;

    @Column(nullable=false, length=120)
    private String titulo;

    @Column(columnDefinition="TEXT")
    private String contenido;

    @Column(name="estado_animo", length=30)
    private String estadoAnimo;

    @Column(length=255)
    private String etiquetas;     // CSV

    @Column(name="fotos_url", length=1024)
    private String fotosUrl;      // CSV

    @Column(nullable=false)
    private boolean eliminado = false;
    
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
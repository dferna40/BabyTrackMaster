package com.babytrackmaster.api_bebe.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bebes")
@Data
public class Bebe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 10)
    private String sexo;

    @Column(name = "peso_nacer")
    private Double pesoNacer;

    @Column(name = "talla_nacer")
    private Double tallaNacer;

    @Column(name = "semanas_gestacion")
    private Integer semanasGestacion;

    @Column(name = "perimetro_craneal_nacer")
    private Double perimetroCranealNacer;

    @Column(name = "peso_actual")
    private Double pesoActual;

    @Column(name = "talla_actual")
    private Double tallaActual;

    @Column(name = "bebe_activo")
    private Boolean bebeActivo = true;

    @Column(name = "numero_ss", length = 50)
    private String numeroSs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_lactancia_id")
    private TipoLactancia tipoLactancia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_alergia_id")
    private TipoAlergia tipoAlergia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_grupo_sanguineo_id")
    private TipoGrupoSanguineo tipoGrupoSanguineo;

    @Column(length = 500)
    private String medicaciones;

    @Column(length = 100)
    private String pediatra;

    @Column(name = "centro_medico", length = 255)
    private String centroMedico;

    @Column(name = "telefono_centro_medico", length = 20)
    private String telefonoCentroMedico;

    @Column(length = 1000)
    private String observaciones;

    @Lob
    @Column(name = "imagen_bebe")
    private byte[] imagenBebe;

    @Column(nullable = false)
    private Boolean eliminado = false;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
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

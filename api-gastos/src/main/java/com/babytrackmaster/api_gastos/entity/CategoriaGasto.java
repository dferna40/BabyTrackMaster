package com.babytrackmaster.api_gastos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categorias_gasto")
@Data
public class CategoriaGasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
}
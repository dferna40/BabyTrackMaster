package com.babytrackmaster.api_gastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.babytrackmaster.api_gastos.entity.CategoriaGasto;

public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {
    @Query("select c from CategoriaGasto c where c.id = :id")
    CategoriaGasto findOneById(Long id);
}
package com.babytrackmaster.api_citas.repository;

import com.babytrackmaster.api_citas.entity.TipoCita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoCitaRepository extends JpaRepository<TipoCita, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}


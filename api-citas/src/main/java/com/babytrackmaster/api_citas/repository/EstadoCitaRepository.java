package com.babytrackmaster.api_citas.repository;

import com.babytrackmaster.api_citas.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCitaRepository extends JpaRepository<EstadoCita, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<EstadoCita> findByNombreIgnoreCase(String nombre);
}

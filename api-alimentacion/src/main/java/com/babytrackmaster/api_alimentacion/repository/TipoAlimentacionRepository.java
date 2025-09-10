package com.babytrackmaster.api_alimentacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;

public interface TipoAlimentacionRepository extends JpaRepository<TipoAlimentacion, Long> {
    Optional<TipoAlimentacion> findByNombreIgnoreCase(String nombre);
}


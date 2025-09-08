package com.babytrackmaster.api_citas.repository;

import com.babytrackmaster.api_citas.entity.TipoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoEspecialidadRepository extends JpaRepository<TipoEspecialidad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}

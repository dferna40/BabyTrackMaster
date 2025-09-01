package com.babytrackmaster.api_bebe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_bebe.entity.Bebe;

public interface BebeRepository extends JpaRepository<Bebe, Long> {
    Optional<Bebe> findByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);
    List<Bebe> findByUsuarioIdAndEliminadoFalseOrderByFechaNacimientoAsc(Long usuarioId);
}

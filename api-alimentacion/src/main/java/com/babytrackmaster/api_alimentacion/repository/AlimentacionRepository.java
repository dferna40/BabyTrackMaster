package com.babytrackmaster.api_alimentacion.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_alimentacion.entity.Alimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;

public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {
    Optional<Alimentacion> findByIdAndUsuarioIdAndBebeIdAndEliminadoFalse(Long id, Long usuarioId, Long bebeId);
    List<Alimentacion> findByUsuarioIdAndBebeIdAndEliminadoFalseOrderByFechaHoraDesc(Long usuarioId, Long bebeId);
    List<Alimentacion> findByUsuarioIdAndBebeIdAndFechaHoraBetweenAndEliminadoFalse(Long usuarioId, Long bebeId, LocalDateTime desde, LocalDateTime hasta);
    List<Alimentacion> findByUsuarioIdAndBebeIdAndTipoAlimentacionIdAndFechaHoraBetweenAndEliminadoFalse(Long usuarioId, Long bebeId, Long tipoAlimentacionId, LocalDateTime desde, LocalDateTime hasta);
    long countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(Long usuarioId, Long bebeId, TipoAlimentacion tipoAlimentacion, LocalDateTime desde, LocalDateTime hasta);
}

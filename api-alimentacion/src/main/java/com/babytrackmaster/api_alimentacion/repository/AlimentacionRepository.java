package com.babytrackmaster.api_alimentacion.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_alimentacion.entity.Alimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;

public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {
    Optional<Alimentacion> findByIdAndUsuarioIdAndBebeIdAndEliminadoFalse(Long id, Long usuarioId, Long bebeId);
    List<Alimentacion> findByUsuarioIdAndBebeIdAndEliminadoFalseOrderByFechaHoraDesc(Long usuarioId, Long bebeId);
    List<Alimentacion> findByUsuarioIdAndBebeIdAndFechaHoraBetweenAndEliminadoFalse(Long usuarioId, Long bebeId, Date desde, Date hasta);
    long countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(Long usuarioId, Long bebeId, TipoAlimentacion tipo, Date desde, Date hasta);
}

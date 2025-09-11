package com.babytrackmaster.api_crecimiento.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_crecimiento.entity.Crecimiento;

public interface CrecimientoRepository extends JpaRepository<Crecimiento, Long> {
    Optional<Crecimiento> findByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);
    List<Crecimiento> findByBebeIdAndUsuarioIdAndEliminadoFalseOrderByFechaDesc(Long bebeId, Long usuarioId);
    List<Crecimiento> findByBebeIdAndUsuarioIdAndEliminadoFalse(Long bebeId, Long usuarioId, Pageable pageable);
    List<Crecimiento> findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalseOrderByFechaDesc(Long bebeId, Long tipoId, Long usuarioId);
    List<Crecimiento> findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalse(Long bebeId, Long tipoId, Long usuarioId, Pageable pageable);
    List<Crecimiento> findByBebeIdAndUsuarioIdAndFechaBetweenAndEliminadoFalseOrderByFechaDesc(Long bebeId, Long usuarioId, Date desde, Date hasta);
    List<Crecimiento> findByBebeIdAndUsuarioIdAndTipo_IdAndEliminadoFalseAndFechaBetween(Long bebeId, Long usuarioId, Long tipoId, Date desde, Date hasta);
}

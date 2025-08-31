package com.babytrackmaster.api_cuidados.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import com.babytrackmaster.api_cuidados.entity.Cuidado;

public interface CuidadoRepository extends JpaRepository<Cuidado, Long> {
    Optional<Cuidado> findByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);
    List<Cuidado> findByBebeIdAndUsuarioIdAndEliminadoFalseOrderByInicioDesc(Long bebeId, Long usuarioId);
    List<Cuidado> findByBebeIdAndUsuarioIdAndEliminadoFalse(Long bebeId, Long usuarioId, Pageable pageable);
    List<Cuidado> findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalseOrderByInicioDesc(Long bebeId, Long tipoId, Long usuarioId);
    List<Cuidado> findByBebeIdAndUsuarioIdAndInicioBetweenAndEliminadoFalseOrderByInicioDesc(Long bebeId, Long usuarioId, Date desde, Date hasta);
}

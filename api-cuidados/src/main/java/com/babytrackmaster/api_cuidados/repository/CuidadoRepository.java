package com.babytrackmaster.api_cuidados.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_cuidados.entity.Cuidado;

public interface CuidadoRepository extends JpaRepository<Cuidado, Long> {
    Optional<Cuidado> findByIdAndEliminadoFalse(Long id);
    List<Cuidado> findByBebeIdAndEliminadoFalseOrderByInicioDesc(Long bebeId);
    List<Cuidado> findByBebeIdAndTipoAndEliminadoFalseOrderByInicioDesc(Long bebeId, String tipo);
    List<Cuidado> findByBebeIdAndInicioBetweenAndEliminadoFalseOrderByInicioDesc(Long bebeId, Date desde, Date hasta);
}

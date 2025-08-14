package com.babytrackmaster.api_cuidados.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_cuidados.entity.Cuidado;

public interface CuidadoRepository extends JpaRepository<Cuidado, Long> {
    List<Cuidado> findByBebeIdOrderByInicioDesc(Long bebeId);
    List<Cuidado> findByBebeIdAndTipoOrderByInicioDesc(Long bebeId, String tipo);
    List<Cuidado> findByBebeIdAndInicioBetweenOrderByInicioDesc(Long bebeId, Date desde, Date hasta);
}

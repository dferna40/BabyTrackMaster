package com.babytrackmaster.api_hitos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_hitos.entity.Hito;

import java.time.LocalDate;
import java.util.List;

public interface HitoRepository extends JpaRepository<Hito, Long> {

    List<Hito> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Hito> findByUsuarioIdAndBebeIdOrderByFechaDesc(Long usuarioId, Long bebeId);

    List<Hito> findByUsuarioIdAndFechaBetweenOrderByFechaDesc(Long usuarioId, LocalDate inicio, LocalDate fin);

    List<Hito> findByUsuarioIdAndTituloContainingIgnoreCaseOrderByFechaDesc(Long usuarioId, String titulo);
}

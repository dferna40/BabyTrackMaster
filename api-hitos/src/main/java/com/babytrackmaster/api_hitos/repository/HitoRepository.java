package com.babytrackmaster.api_hitos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_hitos.entity.Hito;

import java.time.LocalDate;
import java.util.List;

public interface HitoRepository extends JpaRepository<Hito, Long> {
	
	@Query("select h from Hito h where h.id = :id and h.usuarioId = :usuarioId")
	Hito findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);
	
	Hito findFirstByIdAndUsuarioId(Long id, Long usuarioId);

    List<Hito> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Hito> findByUsuarioIdAndBebeIdOrderByFechaDesc(Long usuarioId, Long bebeId);

    List<Hito> findByUsuarioIdAndFechaBetweenOrderByFechaDesc(Long usuarioId, LocalDate inicio, LocalDate fin);

    List<Hito> findByUsuarioIdAndTituloContainingIgnoreCaseOrderByFechaDesc(Long usuarioId, String titulo);
}

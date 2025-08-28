package com.babytrackmaster.api_hitos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_hitos.entity.Hito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HitoRepository extends JpaRepository<Hito, Long> {
	
//	@Query("select h from Hito h where h.id = :id and h.usuarioId = :usuarioId")
//	Hito findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);
//	
//	Hito findByIdAndUsuarioId(Long id, Long usuarioId);
	
        Optional<Hito> findByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);

    Hito findFirstByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);

    boolean existsByIdAndUsuarioIdAndEliminadoFalse(Long id, Long usuarioId);

    List<Hito> findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(Long usuarioId);

    List<Hito> findByUsuarioIdAndBebeIdAndEliminadoFalseOrderByFechaDesc(Long usuarioId, Long bebeId);

    List<Hito> findByUsuarioIdAndFechaBetweenAndEliminadoFalseOrderByFechaDesc(Long usuarioId, LocalDate inicio, LocalDate fin);

    List<Hito> findByUsuarioIdAndTituloContainingIgnoreCaseAndEliminadoFalseOrderByFechaDesc(Long usuarioId, String titulo);
}

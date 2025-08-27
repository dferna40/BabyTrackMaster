package com.babytrackmaster.api_diario.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_diario.entity.Diario;

public interface DiarioRepository extends JpaRepository<Diario, Long> {

    @Query("SELECT d FROM Diario d WHERE d.id = :id AND d.usuarioId = :usuarioId AND d.eliminado = false")
    Diario findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("SELECT d FROM Diario d WHERE d.usuarioId = :usuarioId AND d.eliminado = false " +
           "AND d.fecha BETWEEN :desde AND :hasta ORDER BY d.fecha DESC, d.hora DESC")
    Page<Diario> findByUsuarioAndRangoFechas(@Param("usuarioId") Long usuarioId,
                                             @Param("desde") LocalDate desde,
                                             @Param("hasta") LocalDate hasta,
                                             Pageable pageable);

    @Query("SELECT d FROM Diario d WHERE d.usuarioId = :usuarioId AND d.eliminado = false " +
           "AND d.fecha = :fecha ORDER BY d.hora DESC")
    Page<Diario> findByUsuarioAndDia(@Param("usuarioId") Long usuarioId,
                                     @Param("fecha") LocalDate fecha,
                                     Pageable pageable);

    @Query("SELECT d FROM Diario d WHERE d.usuarioId = :usuarioId AND d.eliminado = false " +
           "AND (:tag IS NULL OR d.etiquetas LIKE %:tag%) ORDER BY d.fecha DESC, d.hora DESC")
    Page<Diario> findByUsuarioAndTag(@Param("usuarioId") Long usuarioId,
                                     @Param("tag") String tag,
                                     Pageable pageable);
}

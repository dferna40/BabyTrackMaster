package com.babytrackmaster.api_citas.repository;

import com.babytrackmaster.api_citas.entity.Cita;
import com.babytrackmaster.api_citas.enums.EstadoCita;
import com.babytrackmaster.api_citas.enums.TipoCita;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("select c from Cita c where c.id = :id and c.usuarioId = :usuarioId and c.eliminado = false")
    Cita findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("select c from Cita c where c.usuarioId = :usuarioId and c.eliminado = false " +
           "and c.fecha between :desde and :hasta " +
           "order by c.fecha asc, c.hora asc")
    Page<Cita> listarPorRango(@Param("usuarioId") Long usuarioId,
                              @Param("desde") LocalDate desde,
                              @Param("hasta") LocalDate hasta,
                              Pageable pageable);

    @Query("select c from Cita c where c.usuarioId = :usuarioId and c.eliminado = false and c.estado = :estado " +
           "order by c.fecha asc, c.hora asc")
    Page<Cita> listarPorEstado(@Param("usuarioId") Long usuarioId,
                               @Param("estado") EstadoCita estado,
                               Pageable pageable);

    @Query("select c from Cita c where c.usuarioId = :usuarioId and c.eliminado = false and c.tipo = :tipo " +
           "order by c.fecha asc, c.hora asc")
    Page<Cita> listarPorTipo(@Param("usuarioId") Long usuarioId,
                             @Param("tipo") TipoCita tipo,
                             Pageable pageable);

    @Query("select c from Cita c where c.usuarioId = :usuarioId and c.eliminado = false and c.medico = :medico " +
           "order by c.fecha asc, c.hora asc")
    Page<Cita> listarPorMedico(@Param("usuarioId") Long usuarioId,
                               @Param("medico") String medico,
                               Pageable pageable);

    @Query("select c from Cita c where c.usuarioId = :usuarioId and c.eliminado = false and " +
           "(c.fecha > :hoy or (c.fecha = :hoy and c.hora >= :hora)) " +
           "order by c.fecha asc, c.hora asc")
    List<Cita> proximas(@Param("usuarioId") Long usuarioId,
                        @Param("hoy") LocalDate hoy,
                        @Param("hora") LocalTime hora);
}
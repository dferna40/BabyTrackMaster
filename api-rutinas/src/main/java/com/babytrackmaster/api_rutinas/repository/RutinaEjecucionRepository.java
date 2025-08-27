package com.babytrackmaster.api_rutinas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_rutinas.entity.RutinaEjecucion;

public interface RutinaEjecucionRepository extends JpaRepository<RutinaEjecucion, Long> {

    @Query("select e from RutinaEjecucion e where e.rutina.id = :rutinaId" +
           " and e.rutina.usuarioId = :usuarioId" +
           " and (:desde is null or e.fechaHora >= :desde)" +
           " and (:hasta is null or e.fechaHora <= :hasta)" +
           " order by e.fechaHora desc")
    List<RutinaEjecucion> buscarHistorial(@Param("rutinaId") Long rutinaId,
                                          @Param("usuarioId") Long usuarioId,
                                          @Param("desde") LocalDateTime desde,
                                          @Param("hasta") LocalDateTime hasta);
}
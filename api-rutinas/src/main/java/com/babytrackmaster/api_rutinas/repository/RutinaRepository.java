package com.babytrackmaster.api_rutinas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_rutinas.entity.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    @Query("select r from Rutina r where r.id = :id and r.usuarioId = :usuarioId and r.bebeId = :bebeId and r.eliminado = false")
    Rutina findOneByIdAndUsuarioAndBebe(@Param("id") Long id,
                                        @Param("usuarioId") Long usuarioId,
                                        @Param("bebeId") Long bebeId);

    @Query("select r from Rutina r where r.usuarioId = :usuarioId and r.bebeId = :bebeId and r.eliminado = false" +
           " and (:activo is null or r.activa = :activo)" +
           " and (:dia is null or r.diasSemana like concat('%', :dia, '%'))")
    Page<Rutina> findAllByUsuarioAndBebeAndFilters(@Param("usuarioId") Long usuarioId,
                                                   @Param("bebeId") Long bebeId,
                                                   @Param("activo") Boolean activo,
                                                   @Param("dia") String dia,
                                                   Pageable pageable);
}
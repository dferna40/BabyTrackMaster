package com.babytrackmaster.api_rutinas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_rutinas.entity.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    @Query("select r from Rutina r where r.id = :id and r.usuarioId = :usuarioId")
    Rutina findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("select r from Rutina r where r.usuarioId = :usuarioId" +
           " and (:activo is null or r.activa = :activo)" +
           " and (:dia is null or r.diasSemana like concat('%', :dia, '%'))")
    Page<Rutina> findAllByUsuarioAndFilters(@Param("usuarioId") Long usuarioId,
                                            @Param("activo") Boolean activo,
                                            @Param("dia") String dia,
                                            Pageable pageable);
}
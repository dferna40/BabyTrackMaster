package com.babytrackmaster.api_gastos.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babytrackmaster.api_gastos.entity.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("select g from Gasto g where g.id = :id and g.usuarioId = :usuarioId and g.eliminado = false")
    Gasto findOneByIdAndUsuario(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("select g from Gasto g where g.usuarioId = :usuarioId and g.fecha between :desde and :hasta and g.eliminado = false")
    Page<Gasto> findByUsuarioAndFechaBetween(@Param("usuarioId") Long usuarioId,
                                             @Param("desde") LocalDate desde,
                                             @Param("hasta") LocalDate hasta,
                                             Pageable pageable);

    @Query("select g from Gasto g where g.usuarioId = :usuarioId and g.categoria.id = :categoriaId and g.eliminado = false")
    Page<Gasto> findByUsuarioAndCategoria(@Param("usuarioId") Long usuarioId,
                                          @Param("categoriaId") Long categoriaId,
                                          Pageable pageable);

    @Query("select coalesce(sum(g.cantidad), 0) from Gasto g where g.usuarioId = :usuarioId and g.fecha between :desde and :hasta and g.eliminado = false")
    BigDecimal sumTotalMes(@Param("usuarioId") Long usuarioId,
                           @Param("desde") LocalDate desde,
                           @Param("hasta") LocalDate hasta);

    @Query("select g.categoria.id, g.categoria.nombre, coalesce(sum(g.cantidad),0) " +
           "from Gasto g where g.usuarioId = :usuarioId and g.fecha between :desde and :hasta and g.eliminado = false " +
           "group by g.categoria.id, g.categoria.nombre order by g.categoria.nombre asc")
    List<Object[]> sumPorCategoriaDelMes(@Param("usuarioId") Long usuarioId,
                                         @Param("desde") LocalDate desde,
                                         @Param("hasta") LocalDate hasta);

    Page<Gasto> findByUsuarioIdAndFechaBetweenAndEliminadoFalseOrderByFechaDesc(Long usuarioId,
            LocalDate desde,
            LocalDate hasta,
            Pageable pageable);
}

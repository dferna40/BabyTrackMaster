package com.babytrackmaster.api_gastos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.entity.Gasto;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.repository.GastoRepository;

@DataJpaTest
class GastoRepositoryTest {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaGastoRepository categoriaRepository;

    @Test
    void shouldExcludeDeletedGastosFromQueries() {
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.setNombre("Test");
        categoria = categoriaRepository.save(categoria);

        LocalDate today = LocalDate.now();

        Gasto activo = new Gasto();
        activo.setUsuarioId(1L);
        activo.setCategoria(categoria);
        activo.setCantidad(new BigDecimal("100.00"));
        activo.setFecha(today);
        gastoRepository.save(activo);

        Gasto eliminado = new Gasto();
        eliminado.setUsuarioId(1L);
        eliminado.setCategoria(categoria);
        eliminado.setCantidad(new BigDecimal("50.00"));
        eliminado.setFecha(today);
        eliminado.setEliminado(true);
        gastoRepository.save(eliminado);

        assertNotNull(gastoRepository.findOneByIdAndUsuario(activo.getId(), 1L));
        assertNull(gastoRepository.findOneByIdAndUsuario(eliminado.getId(), 1L));

        Page<Gasto> page = gastoRepository
                .findByUsuarioIdAndFechaBetweenAndEliminadoFalseOrderByFechaDesc(1L,
                        today.minusDays(1), today.plusDays(1), PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(activo.getId(), page.getContent().get(0).getId());

        BigDecimal total = gastoRepository.sumTotalMes(1L, today.withDayOfMonth(1),
                today.withDayOfMonth(today.lengthOfMonth()));
        assertEquals(new BigDecimal("100.00"), total);

        List<Object[]> rows = gastoRepository.sumPorCategoriaDelMes(1L, today.withDayOfMonth(1),
                today.withDayOfMonth(today.lengthOfMonth()));
        assertEquals(1, rows.size());
        assertEquals(categoria.getId(), ((Number) rows.get(0)[0]).longValue());
        assertEquals(new BigDecimal("100.00"), rows.get(0)[2]);
    }
}

package com.babytrackmaster.api_hitos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.babytrackmaster.api_hitos.entity.Hito;

@DataJpaTest
class HitoRepositoryTest {

    @Autowired
    private HitoRepository repository;

    @Test
    void softDeletedHitosAreExcludedFromQueries() {
        Hito activo = new Hito();
        activo.setUsuarioId(1L);
        activo.setBebeId(1L);
        activo.setTitulo("hito1");
        activo.setFecha(LocalDate.now());
        activo.setDescripcion("desc");
        repository.save(activo);

        Hito eliminado = new Hito();
        eliminado.setUsuarioId(1L);
        eliminado.setBebeId(1L);
        eliminado.setTitulo("hito2");
        eliminado.setFecha(LocalDate.now());
        eliminado.setDescripcion("desc");
        eliminado.setEliminado(true);
        repository.save(eliminado);

        List<Hito> lista = repository.findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(1L);

        assertEquals(1, lista.size());
        assertEquals("hito1", lista.get(0).getTitulo());
    }
}


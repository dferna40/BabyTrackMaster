package com.babytrackmaster.api_bebe.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.babytrackmaster.api_bebe.entity.Bebe;
import com.babytrackmaster.api_bebe.repository.BebeRepository;

@DataJpaTest
class BebeRepositoryTest {

    @Autowired
    private BebeRepository repository;

    @Test
    void softDeletedBebesAreExcludedFromQueries() {
        Bebe activo = new Bebe();
        activo.setUsuarioId(1L);
        activo.setNombre("bebe1");
        activo.setFechaNacimiento(LocalDate.now());
        activo.setSexo("M");
        repository.save(activo);

        Bebe eliminado = new Bebe();
        eliminado.setUsuarioId(1L);
        eliminado.setNombre("bebe2");
        eliminado.setFechaNacimiento(LocalDate.now());
        eliminado.setSexo("F");
        eliminado.setEliminado(true);
        repository.save(eliminado);

        List<Bebe> lista = repository.findByUsuarioIdAndEliminadoFalseOrderByFechaNacimientoAsc(1L);

        assertEquals(1, lista.size());
        assertEquals("bebe1", lista.get(0).getNombre());
    }
}


package com.babytrackmaster.api_cuidados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;

@DataJpaTest
class CuidadoRepositoryTests {

    @Autowired
    private CuidadoRepository repo;

    @Test
    void listarExcluyeEliminados() {
        Date now = new Date();

        Cuidado activo = new Cuidado();
        activo.setBebeId(1L);
        activo.setUsuarioId(1L);
        activo.setTipo("TEST");
        activo.setInicio(now);
        activo.setCreatedAt(now);
        activo.setUpdatedAt(now);
        repo.save(activo);

        Cuidado eliminado = new Cuidado();
        eliminado.setBebeId(1L);
        eliminado.setUsuarioId(1L);
        eliminado.setTipo("TEST");
        eliminado.setInicio(new Date(now.getTime() + 1000));
        eliminado.setCreatedAt(now);
        eliminado.setUpdatedAt(now);
        eliminado.setEliminado(true);
        repo.save(eliminado);

        List<Cuidado> list = repo.findByBebeIdAndEliminadoFalseOrderByInicioDesc(1L);

        assertEquals(1, list.size());
        assertFalse(list.get(0).getEliminado());
    }
}


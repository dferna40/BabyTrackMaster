package com.babytrackmaster.api_rutinas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.babytrackmaster.api_rutinas.entity.Rutina;
import com.babytrackmaster.api_rutinas.repository.RutinaRepository;

@DataJpaTest
class RutinaRepositoryTest {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Test
    void shouldNotReturnDeletedRoutines() {
        Rutina activa = Rutina.builder()
                .usuarioId(1L)
                .nombre("Activa")
                .descripcion("desc")
                .horaProgramada(LocalTime.NOON)
                .diasSemana("L")
                .activa(true)
                .build();
        rutinaRepository.save(activa);

        Rutina eliminada = Rutina.builder()
                .usuarioId(1L)
                .nombre("Eliminada")
                .descripcion("desc")
                .horaProgramada(LocalTime.NOON)
                .diasSemana("M")
                .activa(true)
                .eliminado(true)
                .build();
        rutinaRepository.save(eliminada);

        Page<Rutina> page = rutinaRepository.findAllByUsuarioAndFilters(1L, null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(activa.getNombre(), page.getContent().get(0).getNombre());

        Rutina result = rutinaRepository.findOneByIdAndUsuario(eliminada.getId(), 1L);
        assertNull(result);
    }
}

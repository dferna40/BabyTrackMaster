package com.babytrackmaster.api_bebe.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

    @Test
    void persistAndRetrieveAllFields() {
        byte[] imagen = new byte[]{1, 2, 3};
        Bebe bebe = new Bebe();
        bebe.setUsuarioId(2L);
        bebe.setNombre("test");
        bebe.setFechaNacimiento(LocalDate.of(2024, 1, 1));
        bebe.setSexo("F");
        bebe.setPesoNacer(3.2);
        bebe.setTallaNacer(50.0);
        bebe.setSemanasGestacion(40);
        bebe.setPesoActual(3.2);
        bebe.setTallaActual(50.0);
        bebe.setBebeActivo(false);
        bebe.setNumeroSs("123");
        bebe.setGrupoSanguineo("O+");
        bebe.setMedicaciones("med");
        bebe.setAlergias("ale");
        bebe.setPediatra("doc");
        bebe.setCentroMedico("centro");
        bebe.setTelefonoCentroMedico("555");
        bebe.setObservaciones("obs");
        bebe.setImagenBebe(imagen);
        repository.save(bebe);

        Bebe encontrado = repository.findByIdAndUsuarioIdAndEliminadoFalse(bebe.getId(), 2L).orElseThrow();
        assertEquals(3.2, encontrado.getPesoNacer());
        assertEquals(50.0, encontrado.getTallaNacer());
        assertEquals(40, encontrado.getSemanasGestacion());
        assertEquals(3.2, encontrado.getPesoActual());
        assertEquals(50.0, encontrado.getTallaActual());
        assertEquals(false, encontrado.getBebeActivo());
        assertEquals("123", encontrado.getNumeroSs());
        assertEquals("O+", encontrado.getGrupoSanguineo());
        assertEquals("med", encontrado.getMedicaciones());
        assertEquals("ale", encontrado.getAlergias());
        assertEquals("doc", encontrado.getPediatra());
        assertEquals("centro", encontrado.getCentroMedico());
        assertEquals("555", encontrado.getTelefonoCentroMedico());
        assertEquals("obs", encontrado.getObservaciones());
        assertArrayEquals(imagen, encontrado.getImagenBebe());
    }
}


package com.babytrackmaster.api_bebe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.babytrackmaster.api_bebe.dto.BebeResponse;
import com.babytrackmaster.api_bebe.entity.Bebe;
import com.babytrackmaster.api_bebe.repository.BebeRepository;

@SpringBootTest
class BebeServiceImplTest {

    @Autowired
    private BebeService bebeService;

    @Autowired
    private BebeRepository bebeRepository;

    @AfterEach
    void cleanup() {
        bebeRepository.deleteAll();
    }

    @Test
    void listarExcludesInactiveBabies() {
        Bebe activo = new Bebe();
        activo.setUsuarioId(1L);
        activo.setNombre("Activo");
        activo.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        activo.setSexo("M");

        Bebe inactivo = new Bebe();
        inactivo.setUsuarioId(1L);
        inactivo.setNombre("Inactivo");
        inactivo.setFechaNacimiento(LocalDate.of(2021, 1, 1));
        inactivo.setSexo("M");
        inactivo.setBebeActivo(false);

        bebeRepository.save(activo);
        bebeRepository.save(inactivo);

        List<BebeResponse> result = bebeService.listar(1L);

        assertEquals(1, result.size());
        assertEquals("Activo", result.get(0).getNombre());
    }
}


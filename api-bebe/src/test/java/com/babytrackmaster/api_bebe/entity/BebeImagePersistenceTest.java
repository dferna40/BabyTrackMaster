package com.babytrackmaster.api_bebe.entity;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.babytrackmaster.api_bebe.repository.BebeRepository;

@SpringBootTest
class BebeImagePersistenceTest {

    @Autowired
    private BebeRepository bebeRepository;

    @Test
    void imagePersistsAndRetrievesCorrectly() {
        Bebe bebe = new Bebe();
        bebe.setUsuarioId(1L);
        bebe.setNombre("Imagen");
        bebe.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        bebe.setSexo("M");
        byte[] imagen = new byte[]{1,2,3};
        bebe.setImagenBebe(imagen);

        Bebe saved = bebeRepository.save(bebe);
        Bebe found = bebeRepository.findById(saved.getId()).orElseThrow();

        assertArrayEquals(imagen, found.getImagenBebe());
    }
}


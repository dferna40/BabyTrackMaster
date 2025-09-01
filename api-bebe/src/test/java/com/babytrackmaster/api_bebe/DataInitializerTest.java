package com.babytrackmaster.api_bebe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.babytrackmaster.api_bebe.entity.TipoAlergia;
import com.babytrackmaster.api_bebe.entity.TipoGrupoSanguineo;
import com.babytrackmaster.api_bebe.entity.TipoLactancia;
import com.babytrackmaster.api_bebe.repository.TipoAlergiaRepository;
import com.babytrackmaster.api_bebe.repository.TipoGrupoSanguineoRepository;
import com.babytrackmaster.api_bebe.repository.TipoLactanciaRepository;

@SpringBootTest
class DataInitializerTest {

    @Autowired
    private TipoAlergiaRepository tipoAlergiaRepository;

    @Autowired
    private TipoGrupoSanguineoRepository tipoGrupoSanguineoRepository;

    @Autowired
    private TipoLactanciaRepository tipoLactanciaRepository;

    @Test
    void repositoriesArePopulated() {
        List<String> alergias = tipoAlergiaRepository.findAll().stream()
                .map(TipoAlergia::getNombre)
                .toList();
        assertEquals(7, alergias.size());
        assertTrue(alergias.containsAll(List.of(
                "Ninguna", "Gluten", "Lactosa", "Frutos secos", "Polen", "Ácaros", "Medicamentos")));

        List<String> grupos = tipoGrupoSanguineoRepository.findAll().stream()
                .map(TipoGrupoSanguineo::getNombre)
                .toList();
        assertEquals(8, grupos.size());
        assertTrue(grupos.containsAll(List.of(
                "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")));

        List<String> lactancias = tipoLactanciaRepository.findAll().stream()
                .map(TipoLactancia::getNombre)
                .toList();
        assertEquals(8, lactancias.size());
        assertTrue(lactancias.containsAll(List.of(
                "Lactancia exclusiva", "Lactancia predominante", "Lactancia mixta", "Lactancia complementaria",
                "Lactancia directa", "Lactancia diferida", "Lactancia inducida o relactación", "Lactancia en tándem")));
    }
}

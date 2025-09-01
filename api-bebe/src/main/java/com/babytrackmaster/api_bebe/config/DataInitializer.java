package com.babytrackmaster.api_bebe.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.babytrackmaster.api_bebe.entity.TipoAlergia;
import com.babytrackmaster.api_bebe.entity.TipoGrupoSanguineo;
import com.babytrackmaster.api_bebe.entity.TipoLactancia;
import com.babytrackmaster.api_bebe.repository.TipoAlergiaRepository;
import com.babytrackmaster.api_bebe.repository.TipoGrupoSanguineoRepository;
import com.babytrackmaster.api_bebe.repository.TipoLactanciaRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoAlergiaRepository tipoAlergiaRepository;
    private final TipoGrupoSanguineoRepository tipoGrupoSanguineoRepository;
    private final TipoLactanciaRepository tipoLactanciaRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoAlergiaRepository.count() == 0) {
                tipoAlergiaRepository.saveAll(List.of(
                    createTipoAlergia("Ninguna"),
                    createTipoAlergia("Gluten"),
                    createTipoAlergia("Lactosa"),
                    createTipoAlergia("Frutos secos"),
                    createTipoAlergia("Polen"),
                    createTipoAlergia("Ácaros"),
                    createTipoAlergia("Medicamentos")
                ));
            }

            if (tipoGrupoSanguineoRepository.count() == 0) {
                tipoGrupoSanguineoRepository.saveAll(List.of(
                    createTipoGrupoSanguineo("O+"),
                    createTipoGrupoSanguineo("O-"),
                    createTipoGrupoSanguineo("A+"),
                    createTipoGrupoSanguineo("A-"),
                    createTipoGrupoSanguineo("B+"),
                    createTipoGrupoSanguineo("B-"),
                    createTipoGrupoSanguineo("AB+"),
                    createTipoGrupoSanguineo("AB-")
                ));
            }

            if (tipoLactanciaRepository.count() == 0) {
                tipoLactanciaRepository.saveAll(List.of(
                    createTipoLactancia("Lactancia exclusiva"),
                    createTipoLactancia("Lactancia predominante"),
                    createTipoLactancia("Lactancia mixta"),
                    createTipoLactancia("Lactancia complementaria"),
                    createTipoLactancia("Lactancia directa"),
                    createTipoLactancia("Lactancia diferida"),
                    createTipoLactancia("Lactancia inducida o relactación"),
                    createTipoLactancia("Lactancia en tándem")
                ));
            }
        };
    }

    private TipoAlergia createTipoAlergia(String nombre) {
        TipoAlergia ta = new TipoAlergia();
        ta.setNombre(nombre);
        return ta;
    }

    private TipoGrupoSanguineo createTipoGrupoSanguineo(String nombre) {
        TipoGrupoSanguineo tgs = new TipoGrupoSanguineo();
        tgs.setNombre(nombre);
        return tgs;
    }

    private TipoLactancia createTipoLactancia(String nombre) {
        TipoLactancia tl = new TipoLactancia();
        tl.setNombre(nombre);
        return tl;
    }
}

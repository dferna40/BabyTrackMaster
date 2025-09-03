package com.babytrackmaster.api_citas.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.repository.TipoCitaRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoCitaRepository tipoCitaRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoCitaRepository.count() == 0) {
                tipoCitaRepository.saveAll(List.of(
                    createTipoCita("Vacunación"),
                    createTipoCita("Revisión/Seguimiento"),
                    createTipoCita("Consulta pediátrica"),
                    createTipoCita("Niño sano"),
                    createTipoCita("Urgencia"),
                    createTipoCita("Odontopediatría"),
                    createTipoCita("Especialista"),
                    createTipoCita("Seguimiento nutricional")
                ));
            }
        };
    }

    private TipoCita createTipoCita(String nombre) {
        TipoCita tipo = new TipoCita();
        tipo.setNombre(nombre);
        return tipo;
    }
}


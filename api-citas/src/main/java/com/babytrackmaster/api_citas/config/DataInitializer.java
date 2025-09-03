package com.babytrackmaster.api_citas.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.entity.EstadoCita;
import com.babytrackmaster.api_citas.repository.TipoCitaRepository;
import com.babytrackmaster.api_citas.repository.EstadoCitaRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoCitaRepository tipoCitaRepository;
    private final EstadoCitaRepository estadoCitaRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoCitaRepository.count() == 0) {
                tipoCitaRepository.saveAll(List.of(
                    createTipoCita("Vacunación"),
                    createTipoCita("Revisión/Seguimiento"),
                    createTipoCita("Consulta pediátrica"),
                    createTipoCita("Consulta niño sano"),
                    createTipoCita("Urgencia"),
                    createTipoCita("Odontopediatría"),
                    createTipoCita("Especialista"),
                    createTipoCita("Seguimiento nutricional")
                ));
            }
            if (estadoCitaRepository.count() == 0) {
                estadoCitaRepository.saveAll(List.of(
                    createEstadoCita("Programada"),
                    createEstadoCita("Confirmada"),
                    createEstadoCita("Cancelada"),
                    createEstadoCita("Reprogramada"),
                    createEstadoCita("Completada"),
                    createEstadoCita("No asistida")
                ));
            }
        };
    }

    private TipoCita createTipoCita(String nombre) {
        TipoCita tipo = new TipoCita();
        tipo.setNombre(nombre);
        return tipo;
    }

    private EstadoCita createEstadoCita(String nombre) {
        EstadoCita estado = new EstadoCita();
        estado.setNombre(nombre);
        return estado;
    }
}


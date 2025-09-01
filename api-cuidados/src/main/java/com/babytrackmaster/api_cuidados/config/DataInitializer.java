package com.babytrackmaster.api_cuidados.config;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoCuidadoRepository tipoCuidadoRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoCuidadoRepository.count() == 0) {
                tipoCuidadoRepository.saveAll(List.of(
                    createTipoCuidado("Higiene"),
                    createTipoCuidado("Alimentación"),
                    createTipoCuidado("Sueño"),
                    createTipoCuidado("Salud"),
                    createTipoCuidado("Recreación")
                ));
            }
        };
    }

    private TipoCuidado createTipoCuidado(String nombre) {
        TipoCuidado tc = new TipoCuidado();
        Date now = new Date();
        tc.setNombre(nombre);
        tc.setCreatedAt(now);
        tc.setUpdatedAt(now);
        return tc;
    }
}

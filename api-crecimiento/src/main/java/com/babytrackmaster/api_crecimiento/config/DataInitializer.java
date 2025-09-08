package com.babytrackmaster.api_crecimiento.config;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoCrecimientoRepository tipoCrecimientoRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            List<String> nombres = List.of("Peso", "Talla", "Perímetro cefálico");

            for (String nombre : nombres) {
                if (!tipoCrecimientoRepository.existsByNombre(nombre)) {
                    TipoCrecimiento tc = new TipoCrecimiento();
                    tc.setNombre(nombre);
                    tipoCrecimientoRepository.save(tc);
                }
            }
        };
    }
}